package main;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.*;
import net.dv8tion.jda.api.entities.Message;

public class help extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Long slashId = Long.parseLong("1127968914748473405");
        Long supportId = Long.parseLong("1127962706499088424");
        Long lxbsId = Long.parseLong("1118108459431374898");

        if (command.equals("help")) {
            EmbedBuilder emb = new EmbedBuilder();
            SelectMenu menu = SelectMenu.create("help")
                    .setPlaceholder("Give me information on...")
                    .addOption("Commands", "command", "View all available commands.", Emoji.fromCustom("slash", slashId, false))
                    .addOption("Support", "support", "View support options.", Emoji.fromCustom("support", supportId, false))
                    .build();

            emb.setTitle("We're here to help!")
                .setColor(0xff55ff)
                .setDescription("Welcome to the help center.\nYou can choose what type of help you want.")
                .addField("Email", "support@lxbs.online", true)
                .addField("Website", "[lxbs.online](https://lxbs.online)", true)
                .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");

            event.replyEmbeds(emb.build())
                .addActionRow(menu)
                .addActionRow(Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false)))
                .setEphemeral(true).queue();
        }
    }

    @Override
    public void onSelectMenuInteraction(@Nonnull SelectMenuInteractionEvent event) {
        if (event.getSelectMenu().getId().equals("help")) {
            Message message = event.getMessage();
            EmbedBuilder emb = new EmbedBuilder();
            Long slashId = Long.parseLong("1127968914748473405");
            Long supportId = Long.parseLong("1127962706499088424");
            Long lxbsId = Long.parseLong("1118108459431374898");

            if (event.getValues().get(0).equals("command")) {
                event.deferEdit().setEmbeds(
                    emb.setTitle("LXBS Commands")
                        .setDescription("List of all available commands")
                        .setColor(0xff55ff)
                        .addField("</help:1125414321204236328>", "Get help", false)
                        .addField("</support:1125414321204236336>", "Get support", false)
                        .addField("</rules:1125414321204236330>", "Display server rules", false)
                        .addField("</ip:1125414321204236335>", "Get Minecraft Server IP", false)
                        .addField("</website:1125414321204236329>", "Get the link to our Website", false)
                        .addField("</socials:1125414321204236330>", "Display server rules", false)
                        .addField("</userinfo:1125414321204236331>", "Get info on a user", false)
                        .addField("</pronouns:1125414321359421471>", "Select your pronouns", false)
                        .addField("</colour:1125414321359421472>", "The colour you want to be displayed as", false)
                        .setFooter("LXBS Help", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                        .build())
                    .setActionRows(
                        ActionRow.of(
                            SelectMenu.create("help")
                                .setPlaceholder("Give me information on...")
                                .addOption("Commands", "command", "View all available commands.", Emoji.fromCustom("slash", slashId, false))
                                .addOption("Support", "support", "View support options.", Emoji.fromCustom("support", supportId, false))
                                .build()),
                        ActionRow.of(
                            Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false))))
                    .queue();
            }
            
            if (event.getValues().get(0).equals("support")) {
                event.deferEdit().setEmbeds(
                    emb.setTitle("Support? Sure.")
                        .setColor(0xff55ff)
                        .setDescription("Welcome to the support center.\nIf you have a problem or question you can submit a support Ticket.")
                        .addField("Email", "support@lxbs.online", true)
                        .addField("Website", "https://lxbs.online", true)
                        .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                        .build())
                    .setActionRows(
                        ActionRow.of(
                            SelectMenu.create("help")
                                .setPlaceholder("Give me information on...")
                                .addOption("Commands", "command", "View all available commands.", Emoji.fromCustom("slash", slashId, false))
                                .addOption("Support", "support", "View support options.", Emoji.fromCustom("support", supportId, false))
                                .build()),
                        ActionRow.of(
                            Button.primary("ticket", "Support ticket").withEmoji(Emoji.fromUnicode("U+1F3AB")), 
                            Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false)),
                            Button.link("http://lxbs.online/support", "lxbs.online/support").withEmoji(Emoji.fromCustom("support", supportId, false))))
                    .queue();
            }
        }
    }
}
