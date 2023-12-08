package main.modals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReplyModal extends ListenerAdapter {

    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String ticketId = Objects.requireNonNull(event.getMessage()).getEmbeds().get(0).getTitle();
        if (event.getModalId().equals("reply")) {
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<>();
            Integer localTicketId = Integer.parseInt(ticketId);
            EmbedBuilder emb = new EmbedBuilder();
            String message = Objects.requireNonNull(event.getValue("message")).getAsString();

            User admin = event.getUser();

            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }

            Ticket ticket = map.get(localTicketId);
            List<String> userId = ticket.getUserId();
            List<User> user = new ArrayList<>();
            for (String i : userId) user.add(event.getJDA().retrieveUserById(i).complete());
            StringBuilder stringMention = new StringBuilder();
            for (User i : user) stringMention.append(i.getAsMention()).append(", ");

            emb.setTitle("Ticket Reply" + ticketId)
                    .setColor(0xff55ff)
                    .setDescription(admin.getAsMention() + " has something to say about your ticket. Please make sure to answer them privately, if asked for it.")
                    .addField("Ticket ID", ticketId, false)
                    .addField("Message", message, false)
                    .setFooter("Ticket opened " + ticket.getTimeSubmitted().format(DateTimeFormat));

            for (User i : user) i.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(emb.build())).queue();

            event.reply("Reply sent!").setEphemeral(true).queue();
        }
    }
}
