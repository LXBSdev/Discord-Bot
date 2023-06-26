package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class bday extends ListenerAdapter {
    public static Map<String, String> birthdays;
    public java.time.LocalDate currentDate = java.time.LocalDate.now();
    public Integer dayToday = currentDate.getDayOfMonth();
    public Integer monthToday = currentDate.getMonthValue();
    public Integer yearToday = currentDate.getYear();

    public static void loadBirthdays() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("/birthdays.dat"))) {
            birthdays = (HashMap<String, String>) inputStream.readObject();
        } catch (FileNotFoundException event) {
            birthdays = new HashMap<>();
        } catch (IOException | ClassNotFoundException event) {
            event.printStackTrace();
        }
    }

    private void saveBirthdays() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("/birthdays.dat"))) {
            outputStream.writeObject(birthdays);
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("set-birthday")) {
            String date = event.getOption("date").getAsString();
            String[] birthdayArrey = event.getOption("date").getAsString().split("-");
            Integer year = Integer.parseInt(birthdayArrey[2]);
            String user;
            Integer age = yearToday -= year;

            if (event.getOption("user") != null) {
                user = event.getOption("user").getAsUser().getAsMention();
            } else {
                user = event.getUser().getAsMention();
            }

            birthdays.put(user, date);
            saveBirthdays();

            EmbedBuilder emb = new EmbedBuilder();
            emb.setDescription("Ist notiert, ich werde " + user + "'s **" + age + "th** Geburtstag, am **"
                    + birthdayArrey[0] + "." + birthdayArrey[1] + ".** gratulieren.");

            event.replyEmbeds(emb.build()).queue();
        }

        if (command.equals("check-birthday")) {
            String user;

            if (event.getOption("user") != null) {
                user = event.getOption("user").getAsUser().getAsMention();
            } else {
                user = event.getUser().getAsMention();
            }

            if (birthdays.containsKey(user)) {
                String birthdate = birthdays.get(user);
                if (user == event.getUser().getAsMention()) {
                    event.reply("Dein Geburtstag ist am " + birthdate + "!");
                } else {
                    event.reply(user + "'s Geburtstag ist am " + birthdate + "!");
                }
            } else {
                if (user == event.getUser().getAsMention()) {
                    event.reply(
                            "Dein Geburtstag wurde noch nicht festgelegt. Nutze `/set-birthday <birthdate>` um ihn festzulegen.");
                } else {
                    event.reply(user
                            + "'s Geburtstag wurde noch nicht festgelegt. Nutze `/set-birthday <birthdate> <user>` um ihn festzulegen.");
                }
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("set-birthday", "Set a birthday").addOption(OptionType.STRING, "date",
                "Birthday (ex: dd-MM-yyyy)", true)
                .addOption(OptionType.STRING, "user", "Who's birthday do you want to set", false));
        commandData.add(Commands.slash("check-birthday", "Check for birthday").addOption(OptionType.STRING, "user",
                "Who's birthday do you want to check", false));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("set-birthday", "Set a birthday").addOption(OptionType.STRING, "date",
                "Birthday (ex: dd-MM-yyyy)", true)
                .addOption(OptionType.STRING, "user", "Who's birthday do you want to set", false));
        commandData.add(Commands.slash("check-birthday", "Check for birthday").addOption(OptionType.STRING, "user",
                "Who's birthday do you want to check", false));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
