package main;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class help extends ListenerAdapter {
    
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

            event.replyEmbeds(emb.build()).addActionRow(Button.link("http://lxbs.online", "lxbs.online")).setEphemeral(true).queue();
        }
    }
}
