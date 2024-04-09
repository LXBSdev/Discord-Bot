package main.listeners.tickets;

import main.events.tickets.TicketCreatedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TicketCreatedListener implements main.events.tickets.TicketCreatedListener {
    private static final DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void ticketCreated(TicketCreatedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        EmbedBuilder embedUser = new EmbedBuilder();

        embed.setTitle(event.getTicket().getTicketId().toString())
                .setColor(0xff55ff)
                .setDescription(event.getUser().getAsMention())
                .addField("Topic", event.getTicket().getTopic(), false)
                .addField("Message", event.getTicket().getMessage(), false)
                .setFooter("Ticket opened " + event.getTicket().getTimeSubmitted().format(DateTimeFormat));

        embedUser.setTitle("Ticket submitted")
                .setColor(0xff55ff)
                .setDescription("Your support Ticket has been submitted. Keep your Ticket ID, a member of support might get back to you. You will also be asked for it if you have any further questions to your ticket. You will be informed when the ticket is closed.")
                .addField("Ticket ID", event.getTicket().getTicketId().toString(), false)
                .addField("Topic", event.getTicket().getTopic(), false)
                .addField("Message", event.getTicket().getMessage(), false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(DateTimeFormat));

        event.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embedUser.build())).queue();

        event.getCreationEvent().replyEmbeds(embedUser.build()).setEphemeral(true).queue();

        Objects.requireNonNull(Objects.requireNonNull(event.getCreationEvent().getGuild()).getTextChannelById("1122870579809243196")).sendMessageEmbeds(embed.build())
                .setActionRow(
                        Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                        Button.danger("close", "close ticket"),
                        Button.primary("reply", "reply"))
                .queue();
    }
}
