package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.security.auth.callback.TextInputCallback;
import javax.swing.plaf.TextUI;

import org.apache.commons.collections4.map.LRUMap;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import main.ticket;

public class support extends ListenerAdapter {
    Integer ticketID;

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("ticket")) {
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
        }

        if (command.equals("open-ticket")) {
            loadTickets();
            System.out.println(event.getMember().getRoles().toString());
            if (event.getMember().getRoles().toString().contains("Admin")) {
                List<String> openTickets = tickets.get(false);

                event.reply("Open Ticktes \n" + openTickets);
            } else {
                event.reply("It seems that you do not have the necessary authorisation for this action.")
                        .setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("ticket")) {
            String topic = event.getValue("topic").getAsString();
            String message = event.getValue("message").getAsString();
            User user = event.getUser();
            int lticketId = ticketID + 1;
            ticketID++;

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets.txt"));
                out.writeObject(new ticket(false, lticketId, user, topic, message));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed. Your Ticket has the ID "
                            + ticketID)
                    .setEphemeral(true).queue();
        }
    }
}
