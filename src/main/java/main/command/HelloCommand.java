package main.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HelloCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("hello")) {
            Member member;

            if (event.getOption("user") != null) {
                member = Objects.requireNonNull(event.getOption("user")).getAsMember();
            } else {
                member = event.getMember();
            }

            event.reply("Hi! " + member.getAsMention() + " Nice to see you.").queue();
        }
    }
}
