package main.listeners.buttons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.events.tickets.TicketSolvedEvent;
import main.ticket.Ticket;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

public class CloseButton extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("close")) {
            Message message = event.getMessage();
            Integer ticketId = Integer.parseInt((Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).split(" "))[0]);
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<>();

            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<>() {
                });
                if (map.containsKey(ticketId)) {
                    Ticket ticket = map.get(ticketId);
                    if (ticket.isSolved()) {
                        event.reply("This ticket is already closed.").setEphemeral(true).queue();
                        return;
                    }
                    ticket.ticketSolvedEvent(new TicketSolvedEvent(ticket, ticket, event));
                    ticket.setSolved(true);
                    ticket.setSolvedTime(OffsetDateTime.now());
                    ticket.setTimeWorkedOn(Duration.between(ticket.getTimeSubmitted(), ticket.getTimeClosed()));
                    map.put(ticketId, ticket);
                } else {
                    event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                }
            } catch (MismatchedInputException e) {
                event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("tickets.json"), map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
