package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        long slashId = Long.parseLong("1127968914748473405");
        long supportId = Long.parseLong("1127962706499088424");
        long lxbsId = Long.parseLong("1118108459431374898");

        if (event.getName().equals("help")) {
            EmbedBuilder emb = new EmbedBuilder();
            StringSelectMenu menu = StringSelectMenu.create("help")
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

}
