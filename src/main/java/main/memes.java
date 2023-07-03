package main;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class memes extends ListenerAdapter {

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if (event.isFromGuild()) {
            if (!event.getAuthor().isBot()) {
                if (event.getMessage().getContentStripped().toUpperCase().contains("CHESS")) {
                    event.getMessage().reply(
                            "https://cdn.discordapp.com/attachments/1059574035811414097/1062425543628308500/chessmaster.png")
                            .queue();
                }

                if (event.getMessage().getContentStripped().toUpperCase().contains("RICK")) {
                    event.getMessage().reply(
                            "https://tenor.com/view/rickroll-roll-rick-never-gonna-give-you-up-never-gonna-gif-22954713")
                            .queue();
                }

                if (event.getMessage().getContentStripped().toUpperCase().contains("MATHE")) {
                    event.getMessage().reply(
                            "https://cdn.discordapp.com/attachments/1059574035811414097/1062426087004581979/mathelehrer.jpg")
                            .queue();
                }

                if (event.getMessage().getContentStripped().toUpperCase().contains("NOICH")) {
                    event.getMessage().reply(
                            "https://cdn.discordapp.com/attachments/837779743486378075/1067835776215306270/IMG_3710.png")
                            .queue();
                }

                if (event.getMessage().getContentStripped().toUpperCase().contains("PIGSTEP")) {
                    event.getMessage().reply("https://www.youtube.com/watch?v=PTGGqwTYdc0").queue();
                }

                if (event.getMessage().getContentStripped().toUpperCase().contains("STAL")) {
                    event.getMessage().reply("https://www.youtube.com/watch?v=NhqxP91_CdI").queue();
                }
            }
        }
    }
}