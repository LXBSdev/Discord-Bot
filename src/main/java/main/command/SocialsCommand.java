package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class SocialsCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("socials")) {
            EmbedBuilder emb = new EmbedBuilder();
            long youtubeId = Long.parseLong("771178710979051550");
            long twitterId = Long.parseLong("926980218701348864");
            long tiktokId = Long.parseLong("871842704450027530");
            long redditId = Long.parseLong("811957418878697503");
            long githubId = Long.parseLong("763440516066574338");
            long lxbsId = Long.parseLong("1118108459431374898");

            event.replyEmbeds(
                            emb.setTitle("Socials")
                                    .setDescription("Here you can find the links to all our socials.")
                                    .setColor(0xff55ff)
                                    .addField("Youtube", "[www.youtube.com/@lxbsminecraftserver](https://www.youtube.com/@lxbsminecraftserver)", false)
                                    .addField("Twitter", "[twitter.com/LXBS_Minecraft](https://twitter.com/LXBS_Minecraft)", false)
                                    .addField("TikTok", "[www.tiktok.com/@lxbs_minecraft](https://www.tiktok.com/@lxbs_minecraft)", false)
                                    .addField("Reddit", "[www.reddit.com/r/lxbs](https://www.reddit.com/r/lxbs)", false)
                                    .addField("GitHub", "[www.github.com/LXBSdev](https://www.github.com/LXBSdev)", false)
                                    .setFooter("LXBS socials", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png")
                                    .build())
                    .addActionRow(
                            Button.link("https://www.youtube.com/@lxbsminecraftserver", "Youtube").withEmoji(Emoji.fromCustom("youtube", youtubeId, false)),
                            Button.link("https://twitter.com/LXBS_Minecraft", "Twitter").withEmoji(Emoji.fromCustom("twitter", twitterId, false)),
                            Button.link("https://www.tiktok.com/@lxbs_minecraft", "Twitter").withEmoji(Emoji.fromCustom("tiktok", tiktokId, false))
                    )
                    .addActionRow(
                            Button.link("https://www.reddit.com/r/lxbs", "Twitter").withEmoji(Emoji.fromCustom("reddit", redditId, false)),
                            Button.link("https://www.github.com/LXBSdev", "Twitter").withEmoji(Emoji.fromCustom("github", githubId, false)),
                            Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false))
                    )
                    .setEphemeral(true).queue();
        }
    }
}
