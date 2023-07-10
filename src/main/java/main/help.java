package main;

import javax.annotation.Nonnull;
import javax.swing.text.html.parser.Entity;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.*;

public class help extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("help")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("We're here to help!")
                .setColor(0xff55ff)
                .setDescription("Welcome to the help center.\nYou can choose what type of help you want.")
                .addField("Email", "support@lxbs.online", true)
                .addField("Website", "https://lxbs.online", true)
                .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");

            

            event.replyEmbeds(emb.build())
                .addActionRow(SelectMenu.create("help")
                    .addOption("Commands", "command", "View all available commands.")
                    .addOption("Support", "support", "View support options.")
                    .build())
                .addActionRow(Button.link("http://lxbs.online", "lxbs.online"))
                .setEphemeral(true).queue();
        }
    }

    @Override
    public void onSelectInteraction(@Nonnull SelectMenuInteractionEvent event) {
        if (event.getComponentId().equals("help")) {
            EmbedBuilder emb = new EmbedBuilder();
            if (event.getValues().get(0).equals("command")) {
                event.getMessage().editMessageEmbeds(
                    emb.setTitle("LXBS Commands")
                        .setDescription("List of all available commands")
                        .setColor(0xff55ff)
                        .setThumbnail("https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                        .addField("</help:1125414321204236328>", "Get help", true)
                        .addField("</support:1125414321204236336>", "Get support", true)
                        .addField("</rules:1125414321204236330>", "Display server rules", true)
                        .addField("</userinfo:1125414321204236331>", "Get info on a user", true)
                        .addField("</website:1125414321204236329>", "Get the link to our Website", true)
                        .addField("</ip:1125414321204236335>", "Get Minecraft Server IP", true)
                        .addField("</pronouns:1125414321359421471>", "Select your pronouns", true)
                        .addField("</colour:1125414321359421472>", "The colour you want to be displayed as", true)
                        .addField("", "", false)
                        .addField("More Infos: ", "[lxbs.online](https://lxbs.online)", false)
                        .build())
                    .queue();
            }
        }
    }
}
