package main.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PronounsCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("pronouns")) {
            Member member = event.getMember();
            String pronouns = Objects.requireNonNull(event.getOption("pronouns")).getAsString();

            switch (pronouns.toLowerCase()) {
                case "he":
                    Role he = Objects.requireNonNull(event.getGuild()).getRoleById("1079840705939247155");
                    event.getGuild().addRoleToMember(member, he).queue();
                    event.reply("The pronouns " + he.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "she":
                    Role she = Objects.requireNonNull(event.getGuild()).getRoleById("1079840746946961409");
                    event.getGuild().addRoleToMember(member, she).queue();
                    event.reply("The pronouns " + she.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "they":
                    Role they = Objects.requireNonNull(event.getGuild()).getRoleById("1079840778077097994");
                    event.getGuild().addRoleToMember(member, they).queue();
                    event.reply("The pronouns " + they.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
            }
        }
    }
}
