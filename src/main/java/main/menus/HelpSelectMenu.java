package main.menus;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelpSelectMenu extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (Objects.equals(event.getSelectMenu().getId(), "help")) {
            EmbedBuilder emb = new EmbedBuilder();
            long slashId = Long.parseLong("1127968914748473405");
            long supportId = Long.parseLong("1127962706499088424");
            long lxbsId = Long.parseLong("1118108459431374898");

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
                        .addField("</socials:1128062793778724904>", "Display server rules", false)
                        .addField("</userinfo:1125414321204236331>", "Get info on a user", false)
                        .addField("</pronouns:1125414321359421471>", "Select your pronouns", false)
                        .addField("</colour:1125414321359421472>", "The colour you want to be displayed as", false)
                        .setFooter("LXBS Help", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                        .build()
                        )
                    .queue();
            }

            if (event.getValues().get(0).equals("support")) {

                final List<ItemComponent> actionRowButtonList = new ArrayList<>();
                actionRowButtonList.add(Button.primary("ticket", "Support ticket").withEmoji(Emoji.fromUnicode("U+1F3AB")));
                actionRowButtonList.add(Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false)));
                actionRowButtonList.add(Button.link("http://lxbs.online/support", "lxbs.online/support").withEmoji(Emoji.fromCustom("support", supportId, false)));

                event.deferEdit().setEmbeds(
                    emb.setTitle("Support? Sure.")
                        .setColor(0xff55ff)
                        .setDescription("Welcome to the support center.\nIf you have a problem or question you can submit a support Ticket.")
                        .addField("Email", "support@lxbs.online", true)
                        .addField("Website", "https://lxbs.online", true)
                        .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                        .build()
                        )
                    .setActionRow(
                        StringSelectMenu
                            .create("help")
                            .setPlaceholder("Give me information on...")
                            .addOption("Commands", "command", "View all available commands.", Emoji.fromCustom("slash", slashId, false))
                            .addOption("Support", "support", "View support options.", Emoji.fromCustom("support", supportId, false))
                            .build()
                    )
                    .setActionRow(actionRowButtonList)
                    .queue();
            }
        }
    }
}
