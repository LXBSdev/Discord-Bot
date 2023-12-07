package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class IPCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ip")) {
            EmbedBuilder emb = new EmbedBuilder();

            emb.addField("IP", "You can find our Minecraft server at [lxbs.online](https://lxbs.online)", false);
            event.replyEmbeds(emb.build()).setEphemeral(true).queue();
        }
    }
}
