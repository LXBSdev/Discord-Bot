package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

public class support extends ListenerAdapter {

    JDA jda;
    DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    String globalTicketId;

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("ticket")) {
            TextInput topic = TextInput.create("topic", "Topic", TextInputStyle.SHORT)
                    .setPlaceholder("Subject of this ticket")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Your message")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("ticket", "Support Ticket")
                    .addActionRows(ActionRow.of(topic), ActionRow.of(message))
                    .build();

            event.replyModal(modal).queue();
        }

        if (event.getComponentId().equals("close")) {
            Message message = event.getMessage();
            Integer ticketId = Integer.parseInt((message.getEmbeds().get(0).getTitle().split(" "))[0]);
            EmbedBuilder emb = new EmbedBuilder();
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                if (map.containsKey(ticketId)) {
                    Ticket ticket = map.get(ticketId);
                    if (ticket.isSolved()) {
                        event.reply("This ticket is already closed.").setEphemeral(true).queue();
                    } else {
                        ticket.setSolvedTrue();
                        ticket.setSolvedTime(OffsetDateTime.now());
                        ticket.setTimeWorkedOn(Duration.between(ticket.getTimeSubmitted(), ticket.getTimeClosed()));
                        map.put(ticketId, ticket);
                        String userId = ticket.getUserId();
                        User user = event.getJDA().retrieveUserById(userId).complete();
                        EmbedBuilder embUser = new EmbedBuilder();

                         embUser.setTitle("Ticket solved")
                            .setColor(0xff55ff)
                            .setDescription("Your support Ticket has been solved. The problem should be fixed now, if that shouldn't be the case please contact a member of support.")
                            .addField("Ticket ID", ticket.getTicketId().toString(), false)
                            .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                + " \u2022 Time closed " + OffsetDateTime.now().format(DateTimeFormat));

                        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();
                        message.editMessageEmbeds(
                            emb.setTitle(ticketId + " \u2022 Closed")
                                .setColor(0xff55ff)
                                .setDescription(user.getAsMention())
                                .addField("Topic", ticket.getTopic(), false)
                                .addField("Message", ticket.getMessage(), false)
                                .addField("Time", String.format("%d h %d m", ticket.getTimeWorkedOn().toHours(), ticket.getTimeWorkedOn().toMinutes()), false)
                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                    + " \u2022 Time closed " + OffsetDateTime.now().format(DateTimeFormat))
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

        if (event.getComponentId().equals("refresh")) {
            Message message = event.getMessage();
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            EmbedBuilder emb = new EmbedBuilder();

            if (message.getEmbeds().get(0).getTitle().equals("Open tickets")) {
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
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
                Integer ticketId = Integer.parseInt((message.getEmbeds().get(0).getTitle().split(" "))[0]);
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                    Ticket ticket = map.get(ticketId);
                    User user = event.getJDA().retrieveUserById(ticket.getUserId()).complete();
                    if (ticket.isSolved()) {
                        event.deferEdit().setEmbeds(
                            emb.setTitle(ticketId + " \u2022 Closed")
                                .setColor(0xff55ff)
                                .setDescription(user.getAsMention())
                                .addField("Topic", ticket.getTopic(), false)
                                .addField("Message", ticket.getMessage(), false)
                                .addField("Time", String.format("%d h %d m", ticket.getTimeWorkedOn().toHours(), ticket.getTimeWorkedOn().toMinutes()), false)
                                .setFooter("Time opened " + ticket.getTimeSubmitted().format(DateTimeFormat)
                                    + " \u2022 Time closed "
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

        if (event.getComponentId().equals("reply")) {
            Message msg = event.getMessage();
            String localTicketId = msg.getEmbeds().get(0).getTitle();
            globalTicketId = localTicketId;

            TextInput message = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Your message")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("reply", "Reply to ticket")
                    .addActionRow(message)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("ticket")) {
            String topic = event.getValue("topic").getAsString();
            String message = event.getValue("message").getAsString();
            User user = event.getUser();
            TicketId ticketId = new TicketId(1);
            Map<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
            Map<String, TicketId> ticketIdMap = new HashMap<String, TicketId>();
            EmbedBuilder emb = new EmbedBuilder();
            EmbedBuilder embUser = new EmbedBuilder();
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

            try {
                ticketIdMap = mapper.readValue(new File("ticketId.json"), new TypeReference<Map<String, TicketId>>() {});
                ticketId.setTicketId(ticketIdMap.get("Ticket ID").getTicketId() + 1);
                ticketIdMap.put("Ticket ID", ticketId);
            } catch (FileNotFoundException | MismatchedInputException e) {
                ticketId.setTicketId(1);
                ticketIdMap.put("Ticket ID", ticketId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("ticketId.json"), ticketIdMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Ticket ticket = new Ticket(false, ticketId.getTicketId(), user.getId(), topic, message, OffsetDateTime.now(), null, null);

            try {
                tickets = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                });
                tickets.put(ticketId.getTicketId(), ticket);
            } catch (FileNotFoundException | MismatchedInputException e) {
                tickets.put(ticketId.getTicketId(), ticket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("tickets.json"), tickets);
            } catch (IOException e) {
                e.printStackTrace();
            }

            emb.setTitle(ticketId.getTicketId().toString())
                .setColor(0xff55ff)
                .setDescription(user.getAsMention())
                .addField("Topic", topic, false)
                .addField("Message", message, false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(DateTimeFormat));
            
            embUser.setTitle("Ticket submitted")
                .setColor(0xff55ff)
                .setDescription("Your support Ticket has been submitted. Keep your Ticket ID, a member of support might get back to you. You will also be asked for it if you have any further questions to your ticket. You will be informed when the ticket is closed.")
                .addField("Ticket ID", ticketId.getTicketId().toString(), false)
                .addField("Topic", topic, false)
                .addField("Message", message, false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(DateTimeFormat));

            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();

            event.replyEmbeds(embUser.build()).setEphemeral(true).queue();

            event.getGuild().getTextChannelById("1122870579809243196").sendMessageEmbeds(emb.build())
                .setActionRow(
                    Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                    Button.danger("close", "close ticket"),
                    Button.primary("reply", "reply"))
                .queue();
        }

        if (event.getModalId().equals("reply")) {
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            Integer localTicketId = Integer.parseInt(globalTicketId);
            EmbedBuilder emb = new EmbedBuilder();
            String message = event.getValue("message").toString();
            User admin = event.getUser();

            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }

            Ticket ticket = map.get(localTicketId);
            User user = event.getJDA().retrieveUserById(ticket.getUserId()).complete();

            emb.setTitle("Ticket Reply" + globalTicketId)
                    .setColor(0xff55ff)
                    .setDescription(admin.getAsMention() + " has something to say about your ticket. Please make sure to answer them privately, if asked for it.")
                    .addField("Ticket ID", globalTicketId, false)
                    .addField("Message", message, false)
                    .setFooter("Ticket opened " + ticket.getTimeSubmitted().format(DateTimeFormat));

            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(emb.build())).queue();

            event.reply("Reply sent!").setEphemeral(true).queue();
        }
    }
}
