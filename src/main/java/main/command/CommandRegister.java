package main.command;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class CommandRegister extends ListenerAdapter {

        @Override
        public void onGuildReady(@NotNull GuildReadyEvent event) {
                List<CommandData> commandData = new ArrayList<>();
                commandData.add(Commands.slash("help", "Get help"));
                commandData.add(Commands.slash("website", "Get the link to our Website"));
                commandData.add(Commands.slash("rules", "Display server rules"));
                commandData.add(Commands.slash("userinfo", "Get info on a user")
                                .addOption(OptionType.USER, "user", "The user you want info on", true));
                commandData.add(Commands.slash("hello", "Say hello")
                                .addOption(OptionType.USER, "user", "The user you want to say hello to", false));
                commandData.add(Commands.slash("ip", "Get Minecraft Server IP"));
                commandData.add(Commands.slash("support", "Submit a support formula"));
                commandData.add(Commands.slash("ticket", "Show all open tickets")
                                .addOption(OptionType.INTEGER, "ticket-id",
                                                "The specific ID of a ticket you want to view", false));
                OptionData pronouns = new OptionData(OptionType.STRING, "pronouns", "The pronouns you want to have",
                                true)
                                .addChoice("he/him", "he").addChoice("she/her", "she")
                                .addChoice("they/them", "they");
                commandData.add(Commands.slash("pronouns", "Select your pronouns")
                                .addOptions(pronouns));
                OptionData colour = new OptionData(OptionType.STRING, "colour",
                                "The colour you want to be displayed as", true)
                                .addChoice("#ff55ffForTheWin", "ff55ff").addChoice("purple", "purple")
                                .addChoice("blue", "blue")
                                .addChoice("yellow", "yellow").addChoice("orange", "orange").addChoice("green", "green")
                                .addChoice("gray", "gray").addChoice("black", "black");
                commandData.add(Commands.slash("colour", "The colour you want to be displayed as")
                                .addOptions(colour));
                commandData.add(Commands.context(Command.Type.USER, "Get user information"));
                commandData.add(Commands.slash("socials", "View our social profiles"));
                commandData.add(Commands.slash("sendmessage", "Send a message to a channel")
                        .addOptions(new OptionData(OptionType.CHANNEL, "channel", "The channel", true))
                        .addOptions(new OptionData(OptionType.STRING, "message", "The message", true))
                        .addOptions(new OptionData(OptionType.BOOLEAN, "showauthor", "Show the author", true)));
                commandData.add(Commands.slash("addusertoticket", "Add user to ticket")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user to add", true))
                        .addOptions(new OptionData(OptionType.INTEGER, "ticketid", "The ticket to add to", true)));
                event.getGuild().updateCommands().addCommands(commandData).queue();
        }

        @Override
        public void onGuildJoin(@NotNull GuildJoinEvent event) {
                List<CommandData> commandData = new ArrayList<>();
                commandData.add(Commands.slash("help", "Get help"));
                commandData.add(Commands.slash("website", "Get the link to our Website"));
                commandData.add(Commands.slash("rules", "Display server rules"));
                commandData.add(Commands.slash("userinfo", "Get info on a user")
                                .addOption(OptionType.USER, "user", "The user you want info on", true));
                commandData.add(Commands.slash("hello", "Say hello")
                                .addOption(OptionType.USER, "user", "The user you want to say hello to", false));
                commandData.add(Commands.slash("ip", "Get Minecraft Server IP"));
                commandData.add(Commands.slash("support", "Submit a support formula"));
                commandData.add(Commands.slash("ticket", "Show all open tickets")
                                .addOption(OptionType.INTEGER, "ticket-id",
                                                "The specific ID of a ticket you want to view", false));
                OptionData pronouns = new OptionData(OptionType.STRING, "pronouns", "The pronouns you want to have",
                                true)
                                .addChoice("he/him", "he").addChoice("she/her", "she")
                                .addChoice("they/them", "they");
                commandData.add(Commands.slash("pronouns", "Select your pronouns")
                                .addOptions(pronouns));
                OptionData colour = new OptionData(OptionType.STRING, "colour",
                                "The colour you want to be displayed as", true)
                                .addChoice("#ff55ffForTheWin", "ff55ff").addChoice("purple", "purple")
                                .addChoice("blue", "blue")
                                .addChoice("yellow", "yellow").addChoice("orange", "orange").addChoice("green", "green")
                                .addChoice("gray", "gray").addChoice("black", "black");
                commandData.add(Commands.slash("colour", "The colour you want to be displayed as")
                                .addOptions(colour));
                commandData.add(Commands.context(Command.Type.USER, "Get user information"));
                commandData.add(Commands.slash("socials", "View our social profiles"));
                commandData.add(Commands.slash("sendmessage", "Send a message to a channel")
                        .addOptions(new OptionData(OptionType.CHANNEL, "channel", "The channel"))
                        .addOptions(new OptionData(OptionType.STRING, "message", "The message"))
                        .addOptions(new OptionData(OptionType.BOOLEAN, "show author", "Show the author")));
                commandData.add(Commands.slash("addusertoticket", "Add user to ticket")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user to add", true))
                        .addOptions(new OptionData(OptionType.INTEGER, "ticketid", "The ticket to add to", true)));
                event.getGuild().updateCommands().addCommands(commandData).queue();
        }
}
