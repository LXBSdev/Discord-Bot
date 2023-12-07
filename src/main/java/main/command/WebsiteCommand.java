package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class WebsiteCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("website")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.setTitle("Website")
                    .setDescription("You can find more Information on our [Website](https://lxbs.online)")
                    .setColor(0xff55ff)
                    .setFooter("LXBS Website", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");
            event.replyEmbeds(emb.build()).addActionRow(Button.link("http://lxbs.online", "lxbs.online"))
                    .setEphemeral(true).queue();
        }
    }
}
