package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class support extends ListenerAdapter {

    Integer ticketId;

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals("support")) {
            TextInput topic = TextInput.create("topic", "Topic", TextInputStyle.SHORT)
                    .setPlaceholder("Subject of this ticket")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .setRequired(true)
                    .build();

            TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Your message")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("ticket", "Support Ticket")
                    .addActionRows(ActionRow.of(topic), ActionRow.of(message))
                    .build();

            event.replyModal(modal).queue();
        }

        if (event.getName().equals("ticket")) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Ticket> tickets = new ArrayList<>();
            HashMap<Integer, Ticket> map;
            EmbedBuilder emb = new EmbedBuilder();
            try {
                map = mapper.readValue(new File("tickets.json"),
                        new TypeReference<HashMap<Integer, Ticket>>() {
                        });
                if (event.getOption("ticket-id") != null) {
                    Integer lticketId = event.getOption("ticket-id").getAsInt();
                    for (Ticket value : map.values()) {
                        if (value.getTicketId() == lticketId) {
                            emb.addField("Ticket ID", lticketId.toString(), false);
                            emb.addField("User", value.getUser(), false);
                            emb.addField("Topic", value.getTopic(), false);
                            emb.addField("Message", value.getMessage(), false);
                        }
                    }
                } else {
                    for (Ticket value : map.values()) {
                        if (value.getSolved() == false) {
                            tickets.add(value);
                        }
                    }
                    for (Ticket ticket : tickets) {
                        emb.addField(ticket.getTicketId().toString(), ticket.getTopic(), false);
                    }
                    emb.setTitle("Open tickets")
                            .setColor(0xff55ff);
                    event.replyEmbeds(emb.build()).setEphemeral(false).queue();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                event.reply("There are no tickets avlaible");
            }

        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("ticket")) {
            TextInput topic = TextInput.create("topic", "Topic", TextInputStyle.SHORT)
                    .setPlaceholder("Subject of this ticket")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .setRequired(true)
                    .build();

            TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Your message")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("ticket", "Support Ticket")
                    .addActionRows(ActionRow.of(topic), ActionRow.of(message))
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("ticket")) {
            String topic = event.getValue("topic").getAsString();
            String message = event.getValue("message").getAsString();
            User user = event.getUser();
            Integer lticketId = 0;
            ObjectMapper mapper = new ObjectMapper();
            if (ticketId == null) {
                lticketId = 1;
                ticketId = 1;
            } else {
                ticketId++;
                lticketId = ticketId;
            }

            Ticket ticket = new Ticket(false, lticketId, user, topic, message);
            HashMap<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            map.put(lticketId, ticket);

            try {
                mapper.writeValue(new File("tickets.json"), map);
            } catch (IOException e) {
                e.printStackTrace();
            }

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed.\nYour Ticket has the ID **"
                            + lticketId + "**.\nKeep this ID, a member of support might get back to you.")
                    .setEphemeral(true).queue();
        }
    }
}
