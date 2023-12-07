package main.buttons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class RefreshButton extends ListenerAdapter {

    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getComponentId().equals("refresh")) {
            Message message = event.getMessage();
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map;
            EmbedBuilder emb = new EmbedBuilder();

            if (Objects.equals(message.getEmbeds().get(0).getTitle(), "Open tickets")) {
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<>() {
                    });
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

                    event.deferEdit().setEmbeds(emb.build()).setActionRow(Button.secondary("refresh", Emoji.fromUnicode("U+1F504"))).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Integer ticketId = Integer.parseInt((Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).split(" "))[0]);
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<>() {
                    });
                    Ticket ticket = map.get(ticketId);
                    User user = event.getJDA().retrieveUserById(ticket.getUserId()).complete();
                    if (ticket.isSolved()) {
                        event.deferEdit().setEmbeds(
                                        emb.setTitle(ticketId + " • Closed")
                                                .setColor(0xff55ff)
                                                .setDescription(user.getAsMention())
                                                .addField("Topic", ticket.getTopic(), false)
                                                .addField("Message", ticket.getMessage(), false)
                                                .addField("Time", String.format("%d h %d m", ticket.getTimeWorkedOn().toHours(), ticket.getTimeWorkedOn().toMinutes()), false)
                                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                                        + " • Time closed "
                                                        + ticket.getTimeClosed().format(DateTimeFormat))
                                                .setTimestamp(OffsetDateTime.now())
                                                .build())
                                .setActionRow(Button.secondary("refresh", Emoji.fromUnicode("U+1F504"))).queue();
                    } else {
                        event.deferEdit().setEmbeds(
                                        emb.setTitle(ticketId.toString())
                                                .setColor(0xff55ff)
                                                .setDescription(user.getAsMention())
                                                .addField("Topic", ticket.getTopic(), false)
                                                .addField("Message", ticket.getMessage(), false)
                                                .setFooter("Ticket opened " + ticket.getTimeSubmitted().format(DateTimeFormat))
                                                .setTimestamp(OffsetDateTime.now())
                                                .build())
                                .setActionRow(
                                        Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                                        Button.danger("close", "close ticket"),
                                        Button.primary("reply", "reply"))
                                .queue();
                    }
                } catch (NullPointerException e) {
                    event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
