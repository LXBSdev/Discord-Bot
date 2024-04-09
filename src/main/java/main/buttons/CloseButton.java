package main.buttons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CloseButton extends ListenerAdapter {

    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("close")) {
            Message message = event.getMessage();
            Integer ticketId = Integer.parseInt((Objects.requireNonNull(message.getEmbeds().get(0).getTitle()).split(" "))[0]);
            EmbedBuilder emb = new EmbedBuilder();
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                });
                if (map.containsKey(ticketId)) {
                    Ticket ticket = map.get(ticketId);
                    if (ticket.isSolved()) {
                        event.reply("This ticket is already closed.").setEphemeral(true).queue();
                    } else {
                        ticket.setSolved(true);
                        ticket.setSolvedTime(OffsetDateTime.now());
                        ticket.setTimeWorkedOn(Duration.between(ticket.getTimeSubmitted(), ticket.getTimeClosed()));
                        map.put(ticketId, ticket);
                        List<String> userId = ticket.getUserId();
                        List<User> user = new ArrayList<>();
                        for (String i : userId) user.add(event.getJDA().retrieveUserById(i).complete());
                        StringBuilder stringMention = new StringBuilder();
                        for (User i : user) stringMention.append(i.getAsMention()).append(", ");
                        EmbedBuilder embUser = new EmbedBuilder();

                        embUser.setTitle("Ticket solved")
                                .setColor(0xff55ff)
                                .setDescription("Your support Ticket has been solved. The problem should be fixed now, if that shouldn't be the case please contact a member of support.")
                                .addField("Ticket ID", ticket.getTicketId().toString(), false)
                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                        + " • Time closed " + OffsetDateTime.now().format(DateTimeFormat));

                        for (User i : user) i.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();
                        message.editMessageEmbeds(
                                        emb.setTitle(ticketId + " • Closed")
                                                .setColor(0xff55ff)
                                                .setDescription(stringMention)
                                                .addField("Topic", ticket.getTopic(), false)
                                                .addField("Message", ticket.getMessage(), false)
                                                .addField("Time", String.format("%d h %d m", ticket.getTimeWorkedOn().toHours(), ticket.getTimeWorkedOn().toMinutes()), false)
                                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                                        + " • Time closed " + OffsetDateTime.now().format(DateTimeFormat))
                                                .build())
                                .queue();
                        message.editMessageComponents(
                                        ActionRow.of(
                                                Button.secondary("refresh", Emoji.fromUnicode("U+1F504"))))
                                .queue();
                        event.reply(
                                        "The ticket with the ID **" + ticketId
                                                + "** has been marked as closed\n" + OffsetDateTime.now().format(DateTimeFormat))
                                .setEphemeral(true).queue();
                    }
                } else {
                    event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                }
            } catch (MismatchedInputException e) {
                event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("tickets.json"), map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
