package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RulesCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("rules")) {
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
    }
}
