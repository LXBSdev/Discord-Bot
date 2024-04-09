package main.modals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.events.tickets.TicketCreatedEvent;
import main.listeners.tickets.TicketCreatedListener;
import main.listeners.tickets.TicketSolvedListener;
import main.ticket.Ticket;
import main.ticket.TicketId;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketModal extends ListenerAdapter {
    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getModalId().equals("ticket")) return;

        String topic = Objects.requireNonNull(event.getValue("topic")).getAsString();
        String message = Objects.requireNonNull(event.getValue("message")).getAsString();
        User user = event.getUser();
        TicketId ticketId = new TicketId(1);
        Map<Integer, Ticket> tickets = new HashMap<>();
        Map<String, TicketId> ticketIdMap = new HashMap<>();
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        try {
            ticketIdMap = mapper.readValue(new File("ticketId.json"), new TypeReference<Map<String, TicketId>>() {
            });
            ticketId.setTicketId(ticketIdMap.get("Ticket ID").getTicketId() + 1);
            ticketIdMap.put("Ticket ID", ticketId);
        } catch (FileNotFoundException | MismatchedInputException e) {
            ticketId.setTicketId(1);
            ticketIdMap.put("Ticket ID", ticketId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mapper.writeValue(new File("ticketId.json"), ticketIdMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> ticketIdArray = new ArrayList<>();
        ticketIdArray.add(user.getId());

        Ticket ticket = new Ticket(false, ticketId.getTicketId(), ticketIdArray, topic, message, OffsetDateTime.now(), null, null);
        ticket.addTicketCreatedListener(new TicketCreatedListener());
        ticket.addTicketSolvedListener(new TicketSolvedListener());
        ticket.ticketCreatedEvent(new TicketCreatedEvent(ticket, ticket, user, event));

        try {
            tickets = mapper.readValue(new File("tickets.json"), new TypeReference<>() {
            });
            tickets.put(ticketId.getTicketId(), ticket);
        } catch (FileNotFoundException | MismatchedInputException e) {
            tickets.put(ticketId.getTicketId(), ticket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mapper.writeValue(new File("tickets.json"), tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
