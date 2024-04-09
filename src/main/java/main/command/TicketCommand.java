package main.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketCommand extends ListenerAdapter {

    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket")) {
            if (!Objects.requireNonNull(event.getGuild()).getId().equals("1057684150527733880")) {
                if (Objects.requireNonNull(event.getMember()).getRoles().toString().contains("Admin")) {
                    if (event.getChannel().getId().equals("1122870579809243196") | event.getChannel().getId().equals("1059792277452623872") | event.getChannel().getId().equals("1062121062067863602") | event.getChannel().getId().equals("1125757185113198645")) {
                        ObjectMapper mapper = JsonMapper.builder()
                                .addModule(new JavaTimeModule())
                                .build();
                        Map<Integer, Ticket> map;
                        EmbedBuilder emb = new EmbedBuilder();
                        try {
                            map = mapper.readValue(new File("tickets.json"), new TypeReference<>() {
                            });
                            if (event.getOption("ticket-id") != null) {
                                Integer ticketId = Objects.requireNonNull(event.getOption("ticket-id")).getAsInt();
                                if (map.containsKey(ticketId)) {
                                    Ticket ticket = map.get(ticketId);
                                    List<String> user = new ArrayList<>();
                                    for (String i : ticket.getUserId()) user.add(event.getJDA().retrieveUserById(i).complete().getAsMention());
                                    if (ticket.isSolved()) {
                                        emb.setTitle(ticketId + " • Closed")
                                                .setColor(0xff55ff)
                                                .setDescription(String.join(",", user))
                                                .addField("Topic", ticket.getTopic(), false)
                                                .addField("Message", ticket.getMessage(), false)
                                                .addField("Time ", String.format("%d h %d m", ticket.getTimeWorkedOn().toHours(), ticket.getTimeWorkedOn().toMinutes()), false)
                                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                                        + " • Time closed "
                                                        + OffsetDateTime.now().format(DateTimeFormat));
                                        event.replyEmbeds(emb.build())
                                                .addActionRow(
                                                        Button.secondary("refresh", Emoji.fromUnicode("U+1F504")))
                                                .queue();
                                    } else {
                                        emb.setTitle(ticketId.toString())
                                                .setColor(0xff55ff)
                                                .setDescription(String.join(",", user))
                                                .addField("Topic", ticket.getTopic(), false)
                                                .addField("Message", ticket.getMessage(), false)
                                                .setFooter("Ticket opened " + ticket.getTimeSubmitted().format(DateTimeFormat));
                                        event.replyEmbeds(emb.build())
                                                .addActionRow(
                                                        Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                                                        Button.danger("close", "close ticket"),
                                                        Button.primary("reply", "reply"))
                                                .queue();
                                    }
                                } else {
                                    event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                                }
                            } else {
                                ArrayList<Ticket> tickets = new ArrayList<>();
                                for (Ticket value : map.values()) {
                                    if (!value.isSolved()) {
                                        tickets.add(value);
                                    }
                                }
                                for (Ticket ticket : tickets) {
                                    emb.addField(ticket.getTicketId().toString(), ticket.getTopic(), false);
                                }
                                emb.setTitle("Open tickets")
                                        .setColor(0xff55ff)
                                        .setTimestamp(OffsetDateTime.now());
                                event.replyEmbeds(emb.build())
                                        .addActionRow(
                                                Button.secondary("refresh", Emoji.fromUnicode("U+1F504")))
                                        .queue();
                            }
                        } catch (FileNotFoundException | InvalidDefinitionException | ClassCastException e) {
                            event.reply("There are no tickets available").setEphemeral(true).queue();
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        event.reply("You can only call this method in the channels "
                                        + Objects.requireNonNull(event.getGuild().getTextChannelById("1122870579809243196")).getAsMention()
                                        + ", " + Objects.requireNonNull(event.getGuild().getTextChannelById("1059792277452623872")).getAsMention()
                                        + ", " + Objects.requireNonNull(event.getGuild().getTextChannelById("1062121062067863602")).getAsMention()
                                        + " or " + Objects.requireNonNull(event.getGuild().getTextChannelById("1125757185113198645")).getAsMention())
                                .setEphemeral(true)
                                .queue();
                    }
                } else {
                    event.reply("You don't have permission to do that!").setEphemeral(true).queue();
                }
            } else {
                event.reply("You aren't in the right server for that!").setEphemeral(true).queue();
            }
        }
    }
}
