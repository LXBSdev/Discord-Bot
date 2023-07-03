package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class commands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("help")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("LXBS Commands");
            emb.setDescription("List of all available commands");
            emb.setColor(0xff55ff);
            emb.setThumbnail(
                    "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");
            emb.addField("/help", "Get help", true);
            emb.addField("/support", "Get support", true);
            emb.addField("/rules", "Display server rules", true);
            emb.addField("/userinfo", "Get info on a user", true);
            emb.addField("/website", "Get the link to our Website", true);
            emb.addField("/respect", "Pay respect", true);
            emb.addField("/rickroll", "Rick-roll someone", true);
            emb.addField("/ip", "Get Minecraft Server IP", true);
            emb.addField("/pronouns", "Select your pronouns", true);
            emb.addField("/colour", "The colour you want to be displayed as", true);
            emb.addField("", "", false);
            emb.addField("More Infos: ", "[lxbs.online](https://lxbs.online)", false);

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }

        if (command.equals("website")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.addField("Website", "You can find more Information on our [Website](https://lxbs.online)", false);

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }

        if (command.equals("rules")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("LXBS Server Rules");
            emb.setDescription("List of all Server Rules");
            emb.setColor(0x212e94);
            emb.setThumbnail(
                    "https://cdn.discordapp.com/attachments/837779743486378075/1066785374317334689/4385330_Rules1280.png");
            emb.addField("1.", "Respektvoller Umgang miteinander", false);
            emb.addField("2.", "kein Rassismus/Nationalsozialismus", false);
            emb.addField("3.", "kein Spam", false);
            emb.addField("4.",
                    "wer mich \"Otto\" oder \"Flo/Florian\" ohne meine Berechtigung nennt bekommt Time-out von min. 5 Minuten. (man darf mich so nennen wenn amn die \"YESSSSSSSSSSSSS\" Rolle hat)",
                    false);
            emb.addField("5.",
                    "Leute dürfen nicht als \"Master/Mommy/Daddy\" bezeichnet werden wenn sie nicht die \"you can \"Master\" me..\" Rolle haben. auch hier kann ein Time-out als strafe gegeben werden. !!!bei Verstoß gegen die regeln werde ich verwarnen und/oder die Person Bannen!!!",
                    false);
            emb.addField("6.", "Bitte respektiere unseren Support", false);
            emb.setFooter("You can find more details in #rules");

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }

        if (command.equals("userinfo")) {
            User user = event.getOption("user").getAsUser();
            Member member = event.getOption("user").getAsMember();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Member Info");
            emb.setDescription(user.getName() + " joined on " + member.getTimeJoined().format(fmt));
            emb.setColor(member.getColor());
            emb.setThumbnail(user.getAvatarUrl());
            emb.setAuthor("Information on " + user.getAsTag());
            emb.addField("Nickname: ", member.getNickname() == null ? "No Nickname" : member.getNickname(), false);
            emb.addField("Status: ", member.getOnlineStatus().toString(), false);
            emb.addField("Game: ", getActivities(member.getActivities()), false);
            emb.addField("Roles: ", getRolesAsString(member.getRoles()), false);
            emb.addField("Server Bost: ", member.getTimeBoosted() == null ? "Has never boosted this Server"
                    : member.getTimeBoosted().format(fmt), false);

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }

        if (command.equals("hello")) {
            Member member;

            if (event.getOption("user") != null) {
                member = event.getOption("user").getAsMember();
            } else {
                member = event.getMember();
            }

            event.reply("Hi! " + member.getAsMention() + " Nice to see you.").queue();
        }

        if (command.equals("rickroll")) {
            Member member = event.getOption("user").getAsMember();
            event.reply(member.getAsMention()
                    + "\n https://tenor.com/view/rickroll-roll-rick-never-gonna-give-you-up-never-gonna-gif-22954713")
                    .queue();
        }

        if (command.equals("respect")) {
            Member member = event.getOption("user").getAsMember();
            event.reply(member.getAsMention()
                    + "\n https://tenor.com/view/keyboard-hyperx-rgb-hyperx-family-hyperx-gaming-gif-17743649").queue();
        }

        if (command.equals("ip")) {

            EmbedBuilder emb = new EmbedBuilder();

            emb.addField("IP", "You can find our Minecraft server at [lxbs.online](https://lxbs.online)", false);

            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }

        if (command.equals("pronouns")) {
            Member member = event.getMember();
            String pronouns = event.getOption("pronouns").getAsString();

            switch (pronouns.toLowerCase()) {
                case "he":
                    Role he = event.getGuild().getRoleById("1079840705939247155");
                    event.getGuild().addRoleToMember(member, he).queue();
                    event.reply("The pronouns " + he.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "she":
                    Role she = event.getGuild().getRoleById("1079840746946961409");
                    event.getGuild().addRoleToMember(member, she).queue();
                    event.reply("The pronouns " + she.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "they":
                    Role they = event.getGuild().getRoleById("1079840778077097994");
                    event.getGuild().addRoleToMember(member, they).queue();
                    event.reply("The pronouns " + they.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;
            }
        }

        if (command.equals("colour")) {
            Member member = event.getMember();
            String pronouns = event.getOption("colour").getAsString();

            switch (pronouns.toLowerCase()) {
                case "ff55ff":
                    Role ff55ff = event.getGuild().getRoleById("1122511805982457866");
                    event.getGuild().addRoleToMember(member, ff55ff).queue();
                    event.reply("The colour " + ff55ff.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "purple":
                    Role purple = event.getGuild().getRoleById("1122511747107000370");
                    event.getGuild().addRoleToMember(member, purple).queue();
                    event.reply("The pronouns " + purple.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "blue":
                    Role blue = event.getGuild().getRoleById("1122511895669256233");
                    event.getGuild().addRoleToMember(member, blue).queue();
                    event.reply("The colour " + blue.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "yellow":
                    Role yellow = event.getGuild().getRoleById("1122511956784447596");
                    event.getGuild().addRoleToMember(member, yellow).queue();
                    event.reply("The colour " + yellow.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "orange":
                    Role orange = event.getGuild().getRoleById("1122512010828070962");
                    event.getGuild().addRoleToMember(member, orange).queue();
                    event.reply("The colour " + orange.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "green":
                    Role green = event.getGuild().getRoleById("1122512139781935179");
                    event.getGuild().addRoleToMember(member, green).queue();
                    event.reply("The colour " + green.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "gray":
                    Role gray = event.getGuild().getRoleById("1122512045435273387");
                    event.getGuild().addRoleToMember(member, gray).queue();
                    event.reply("The colour " + gray.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "black":
                    Role black = event.getGuild().getRoleById("1122513483305582712");
                    event.getGuild().addRoleToMember(member, black).queue();
                    event.reply("The colour " + black.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;
            }
        }
    }

    private String getActivities(List activitiesList) {
        String activitie = "";
        if (!activitiesList.isEmpty()) {
            Activity tempActiv = (Activity) activitiesList.get(0);
            for (int i = 1; i < activitiesList.size(); i++) {
                tempActiv = (Activity) activitiesList.get(i);
                activitie = activitie + ", " + tempActiv;
            }
        } else {
            activitie = "No activitie";
        }
        return activitie;
    }

    private String getRolesAsString(List rolesList) {
        String roles = "";
        if (!rolesList.isEmpty()) {
            Role tempRole = (Role) rolesList.get(0);
            roles = tempRole.getAsMention();
            for (int i = 1; i < rolesList.size(); i++) {
                tempRole = (Role) rolesList.get(i);
                roles = roles + ", " + tempRole.getAsMention();
            }
        } else {
            roles = "No roles";
        }
        return roles;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("help", "Get help"));
        commandData.add(Commands.slash("website", "Get the link to our Website"));
        commandData.add(Commands.slash("rules", "Display server rules"));
        commandData.add(Commands.slash("userinfo", "Get info on a user").addOption(OptionType.USER, "user",
                "The user you want info on", true));
        commandData.add(Commands.slash("hello", "Say hello").addOption(OptionType.USER, "user",
                "The user you want to say hello to", false));
        commandData.add(Commands.slash("rickroll", "Rick-roll someone").addOption(OptionType.USER, "user",
                "who you want to pay respect to", true));
        commandData.add(Commands.slash("respect", "Pay respect").addOption(OptionType.USER, "user",
                "who you want to pay respect to", true));
        commandData.add(Commands.slash("ip", "Get Minecraft Server IP"));
        commandData.add(Commands.slash("support", "Submit a support formula").addOption(OptionType.STRING, "topic",
                "What topic seems your problem to be of", true)
                .addOption(OptionType.STRING, "message", "Descreibe your problem", true));
        commandData.add(Commands.slash("open-ticket", "Show all open tickets"));
        OptionData pronouns = new OptionData(OptionType.STRING, "pronouns", "The pronouns you want to have", true)
                .addChoice("he/him", "he").addChoice("she/her", "she").addChoice("they/them", "they");
        commandData.add(Commands.slash("pronouns", "Select your pronouns").addOptions(pronouns));
        OptionData colour = new OptionData(OptionType.STRING, "colour", "The colour you want to be displayed as", true)
                .addChoice("#ff55ffForTheWin", "ff55ff").addChoice("purple", "purple").addChoice("blue", "blue")
                .addChoice("yellow", "yellow").addChoice("orange", "orange").addChoice("green", "green")
                .addChoice("gray", "gray").addChoice("black", "black");
        commandData.add(Commands.slash("colour", "The colour you want to be displayed as").addOptions(colour));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("help", "Get help"));
        commandData.add(Commands.slash("website", "Get the link to our Website"));
        commandData.add(Commands.slash("rules", "Display server rules"));
        commandData.add(Commands.slash("userinfo", "Get info on a user").addOption(OptionType.USER, "user",
                "The user you want info on", true));
        commandData.add(Commands.slash("hello", "Say hello").addOption(OptionType.USER, "user",
                "The user you want to say hello to", false));
        commandData.add(Commands.slash("rickroll", "Rick-roll someone").addOption(OptionType.USER, "user",
                "who you want to pay respect to", true));
        commandData.add(Commands.slash("respect", "Pay respect").addOption(OptionType.USER, "user",
                "who you want to pay respect to", true));
        commandData.add(Commands.slash("ip", "Get Minecraft Server IP"));
        commandData.add(Commands.slash("support", "Submit a support formula").addOption(OptionType.STRING, "topic",
                "The topic of your problem", true)
                .addOption(OptionType.STRING, "message", "Descreibe your problem", true));
        commandData.add(Commands.slash("open-ticket", "Show all open tickets"));
        OptionData pronouns = new OptionData(OptionType.STRING, "pronouns", "The pronouns you want to have", true)
                .addChoice("he/him", "he").addChoice("she/her", "she").addChoice("they/them", "they");
        commandData.add(Commands.slash("pronouns", "Select your pronouns").addOptions(pronouns));
        OptionData colour = new OptionData(OptionType.STRING, "colour", "The colour you want to be displayed as", true)
                .addChoice("#ff55ffForTheWin", "ff55ff").addChoice("purple", "purple").addChoice("blue", "blue")
                .addChoice("yellow", "yellow").addChoice("orange", "orange").addChoice("green", "green")
                .addChoice("gray", "gray").addChoice("black", "black");
        commandData.add(Commands.slash("colour", "The colour you want to be displayed as").addOptions(colour));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

}
