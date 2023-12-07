package main.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ColourCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("colour")) {
            Member member = event.getMember();
            String pronouns = Objects.requireNonNull(event.getOption("colour")).getAsString();

            switch (pronouns.toLowerCase()) {
                case "ff55ff":
                    Role ff55ff = Objects.requireNonNull(event.getGuild()).getRoleById("1122511805982457866");
                    event.getGuild().addRoleToMember(member, ff55ff).queue();
                    event.reply("The colour " + ff55ff.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "purple":
                    Role purple = Objects.requireNonNull(event.getGuild()).getRoleById("1122511747107000370");
                    event.getGuild().addRoleToMember(member, purple).queue();
                    event.reply("The pronouns " + purple.getAsMention() + " have been added to your profile.")
                            .setEphemeral(true).queue();
                    return;

                case "blue":
                    Role blue = Objects.requireNonNull(event.getGuild()).getRoleById("1122511895669256233");
                    event.getGuild().addRoleToMember(member, blue).queue();
                    event.reply("The colour " + blue.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "yellow":
                    Role yellow = Objects.requireNonNull(event.getGuild()).getRoleById("1122511956784447596");
                    event.getGuild().addRoleToMember(member, yellow).queue();
                    event.reply("The colour " + yellow.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "orange":
                    Role orange = Objects.requireNonNull(event.getGuild()).getRoleById("1122512010828070962");
                    event.getGuild().addRoleToMember(member, orange).queue();
                    event.reply("The colour " + orange.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "green":
                    Role green = Objects.requireNonNull(event.getGuild()).getRoleById("1122512139781935179");
                    event.getGuild().addRoleToMember(member, green).queue();
                    event.reply("The colour " + green.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "gray":
                    Role gray = Objects.requireNonNull(event.getGuild()).getRoleById("1122512045435273387");
                    event.getGuild().addRoleToMember(member, gray).queue();
                    event.reply("The colour " + gray.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
                    return;

                case "black":
                    Role black = Objects.requireNonNull(event.getGuild()).getRoleById("1122513483305582712");
                    event.getGuild().addRoleToMember(member, black).queue();
                    event.reply("The colour " + black.getAsMention() + " was assigned to you.")
                            .setEphemeral(true).queue();
            }
        }
    }
}
