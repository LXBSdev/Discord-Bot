package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Nonnull;
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
            Integer lticketId = 1;

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("tickets.txt"));
                Integer ticketId = (Integer) in.readObject() + 1;
                in.close();
                ticketId ticketIdObject = new ticketId(ticketId);
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets.txt"));
                    out.writeObject(ticketIdObject);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                ticketId ticketId = new ticketId(1);
                lticketId = ticketId.getTicketId();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets.txt"));
                    out.writeObject(ticketId);
                    out.close();
                } catch (IOException ee) {
                    e.printStackTrace();
                }
            }

            ticket ticket = new ticket(false, lticketId, user, topic, message);

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets.txt"));
                out.writeObject(ticket);
                out.close();
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
