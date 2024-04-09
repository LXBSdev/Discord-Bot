package main.listeners.tickets;

import main.events.tickets.TicketSolvedEvent;
import main.jda.JDA;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This listener can receive a {@link TicketSolvedEvent}<br>
 * Upon receiving said event the listener sends a message to each user on the ticket.<br>
 * <br>
 * Should the {@link TicketSolvedEvent} also contain the event that closed or solved the ticket,
 * it also updates the original message and responses to it.
 *
 * @author wyatt_was
 * @version 2024.4
 */
public class TicketSolvedListener implements main.events.tickets.TicketSolvedListener {
    private static final DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void ticketSolved(TicketSolvedEvent event) {
        EmbedBuilder embedUser = new EmbedBuilder();
        List<User> users = new ArrayList<>();
        List<String> usersAsMention = new ArrayList<>();

        for (String user : event.getTicket().getUserId())
            users.add(new JDA().getJDA().retrieveUserById(user).complete());
        for (User user : users) usersAsMention.add(user.getAsMention());

        String userAsMentionList = String.join(",", usersAsMention);

        embedUser.setTitle("Ticket solved")
                .setColor(0xff55ff)
                .setDescription("Your support Ticket has been solved. The problem should be fixed now, if that shouldn't be the case please contact a member of support.")
                .addField("Ticket ID", event.getTicket().getTicketId().toString(), false)
                .setFooter("Time opened " + event.getTicket().getTimeSubmitted().format(DateTimeFormat)
                        + " • Time closed " + OffsetDateTime.now().format(DateTimeFormat));

        for (User user : users)
            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embedUser.build())).queue();

        if (event.getClosingEvent() != null) {
            Message message = event.getClosingEvent().getMessage();
            EmbedBuilder embed = new EmbedBuilder();

            message.editMessageEmbeds(
                            embed.setTitle(event.getTicket().getTicketId() + " • Closed")
                                    .setColor(0xff55ff)
                                    .setDescription(userAsMentionList)
                                    .addField("Topic", event.getTicket().getTopic(), false)
                                    .addField("Message", event.getTicket().getMessage(), false)
                                    .addField("Time", String.format("%d h %d m", event.getTicket().getTimeWorkedOn().toHours(), event.getTicket().getTimeWorkedOn().toMinutes()), false)
                                    .setFooter("Time opened " + event.getTicket().getTimeSubmitted().format(DateTimeFormat)
                                            + " • Time closed " + OffsetDateTime.now().format(DateTimeFormat))
                                    .build())
                    .queue();

            message.editMessageComponents(
                            ActionRow.of(
                                    Button.secondary("refresh", Emoji.fromUnicode("U+1F504")))
                    )
                    .queue();

            event.getClosingEvent().reply(
                            "The ticket with the ID **" + event.getTicket().getTicketId()
                                    + "** has been marked as closed\n" + OffsetDateTime.now().format(DateTimeFormat))
                    .setEphemeral(true).queue();
        }
    }
}
