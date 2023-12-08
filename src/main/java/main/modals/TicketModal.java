package main.modals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.ticket.Ticket;
import main.ticket.TicketId;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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
        if (event.getModalId().equals("ticket")) {
            String topic = Objects.requireNonNull(event.getValue("topic")).getAsString();
            String message = Objects.requireNonNull(event.getValue("message")).getAsString();
            User user = event.getUser();
            TicketId ticketId = new TicketId(1);
            Map<Integer, Ticket> tickets = new HashMap<>();
            Map<String, TicketId> ticketIdMap = new HashMap<>();
            EmbedBuilder emb = new EmbedBuilder();
            EmbedBuilder embUser = new EmbedBuilder();
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

            emb.setTitle(ticketId.getTicketId().toString())
                    .setColor(0xff55ff)
                    .setDescription(user.getAsMention())
                    .addField("Topic", topic, false)
                    .addField("Message", message, false)
                    .setFooter("Ticket opened " + OffsetDateTime.now().format(DateTimeFormat));

            embUser.setTitle("Ticket submitted")
                    .setColor(0xff55ff)
                    .setDescription("Your support Ticket has been submitted. Keep your Ticket ID, a member of support might get back to you. You will also be asked for it if you have any further questions to your ticket. You will be informed when the ticket is closed.")
                    .addField("Ticket ID", ticketId.getTicketId().toString(), false)
                    .addField("Topic", topic, false)
                    .addField("Message", message, false)
                    .setFooter("Ticket opened " + OffsetDateTime.now().format(DateTimeFormat));

            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();

            event.replyEmbeds(embUser.build()).setEphemeral(true).queue();

            Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById("1122870579809243196")).sendMessageEmbeds(emb.build())
                    .setActionRow(
                            Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                            Button.danger("close", "close ticket"),
                            Button.primary("reply", "reply"))
                    .queue();
        }
    }
}
