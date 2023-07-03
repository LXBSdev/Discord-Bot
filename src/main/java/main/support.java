package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import main.ticket;

public class support extends ListenerAdapter {
    private static Map<Boolean, List<String>> tickets;

    public static void loadTickets() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("tickets.dat"))) {
            tickets = (HashMap<Boolean, List<String>>) inputStream.readObject();
        } catch (FileNotFoundException event) {
            tickets = new HashMap<>();
        } catch (IOException | ClassNotFoundException event) {
            event.printStackTrace();
        }
    }

    private void saveTickets() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("tickets.dat"))) {
            outputStream.writeObject(tickets);
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Integer ticketID = 0;

        if (command.equals("support")) {
            String topic = event.getOption("topic").getAsString();
            String message = event.getOption("message").getAsString();
            User user = event.getUser();
            ticketID++;

            new ticket(false, ticketID, user, topic, message);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(""))

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed. Your Ticket has the ID "
                            + ticketID)
                    .setEphemeral(true).queue();
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
}
