package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class SupportCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("support")) {
            EmbedBuilder emb = new EmbedBuilder();
            long lxbsId = Long.parseLong("1118108459431374898");
            long supportId = Long.parseLong("1127962706499088424");

            emb.setTitle("Support? Sure.")
                    .setColor(0xff55ff)
                    .setDescription("Welcome to the support center.\nIf you have a problem or question you can submit a support Ticket.")
                    .addField("Email", "support@lxbs.online", true)
                    .addField("Website", "[lxbs.online](https://lxbs.online)", true)
                    .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");

            event.replyEmbeds(emb.build()).addActionRow(
                            Button.primary("ticket", "Support ticket").withEmoji(Emoji.fromUnicode("U+1F3AB")),
                            Button.link("http://lxbs.online/support", "lxbs.online/support").withEmoji(Emoji.fromCustom("support", supportId, false)),
                            Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false)))
                    .setEphemeral(true).queue();
        }
    }
}
