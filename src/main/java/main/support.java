package main;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        EmbedBuilder supportFormList = new EmbedBuilder();
        Integer ticketID = 0;

        if (command.equals("support")) {
            String topic = event.getOption("topic").getAsString();
            String message = event.getOption("message").getAsString();
            User user = event.getUser();
            Integer eTicketID = ticketID + 1;
            ticketID = eTicketID;
            
            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed. Your Ticket has the ID "
                            + eTicketID)
                    .setEphemeral(true).queue();

            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Support Form");
            emb.setDescription("User: " + user.getAsMention());
            emb.addField("Topic: ", topic, false);
            emb.addField("Message: ", message, false);
            emb.addField("TicketID: ", eTicketID.toString(), false);
            event.getGuild().getTextChannelById("1059792277452623872").sendMessageEmbeds(emb.build()).queue();

            supportFormList.addField("Topic: ", topic, false);
            supportFormList.addField("Message: ", message, false);
            supportFormList.addField("TicketID", eTicketID.toString(), false);

        }

        if (command.equals("ticket")) {
            System.out.println(event.getMember().getRoles());
            if (event.getMember().getRoles().toString().contains("Admin")) {
                supportFormList.setTitle("Open Tickets");
                event.replyEmbeds(supportFormList.build()).queue();
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
        commandData.add(Commands.slash("ticket", "Show all open tickets").addOption(OptionType.INTEGER, "ticketid",
                "The ID to a specific ticket", false));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("support", "Submit a support formula").addOption(OptionType.STRING, "topid",
                "The topic of your problem", true)
                .addOption(OptionType.STRING, "message", "Descreibe your problem", true));
        commandData.add(Commands.slash("ticket", "Show all open tickets").addOption(OptionType.INTEGER, "ticketid",
                "The ID to a specific ticket", false));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
