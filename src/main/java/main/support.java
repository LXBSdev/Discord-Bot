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

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Integer ticketID = 0;

        if (command.equals("support")) {
            String topic = event.getOption("topic").getAsString();
            String message = event.getOption("message").getAsString();
            User user = event.getUser();
            ticketID++;

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed. Your Ticket has the ID "
                            + ticketID)
                    .setEphemeral(true).queue();

            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Support Form");
            emb.setDescription("User: " + user.getAsMention());
            emb.addField("Topic: ", topic, false);
            emb.addField("Message: ", message, false);
            emb.addField("TicketID: ", ticketID.toString(), false);
            event.getGuild().getTextChannelById("1122870579809243196").sendMessageEmbeds(emb.build()).queue();

            List<String> ticket = new ArrayList<>();
            ticket.add("user: " + user.toString());
            ticket.add("topic: " + topic);
            ticket.add("message: " + message);
            ticket.add("ticket ID: " + ticketID.toString());

            tickets.put(false, ticket);
            saveTickets();
        }

        if (command.equals("open-ticket")) {
            loadTickets();
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
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("support", "Submit a support formula").addOption(OptionType.STRING, "topic",
                "What topic seems your problem to be of", true)
                .addOption(OptionType.STRING, "message", "Descreibe your problem", true));
        commandData.add(Commands.slash("open-ticket", "Show all open tickets"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("support", "Submit a support formula").addOption(OptionType.STRING, "topid",
                "The topic of your problem", true)
                .addOption(OptionType.STRING, "message", "Descreibe your problem", true));
        commandData.add(Commands.slash("open-ticket", "Show all open tickets"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
