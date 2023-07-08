package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
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

    Integer ticketId;
    JDA jda;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals("support")) {
            TextInput topic = TextInput.create("topic", "Topic", TextInputStyle.SHORT)
                    .setPlaceholder("Subject of this ticket")
                    .setMinLength(1)
                    .setMaxLength(100)
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

        if (event.getName().equals("ticket")) {
            if (event.getMember().getRoles().toString().contains("Admin")) {
                if (event.getChannel().getId().equals("1122870579809243196")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
                    EmbedBuilder emb = new EmbedBuilder();
                    try {
                        map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                        if (event.getOption("ticket-id") != null) {
                            Integer ticketId = event.getOption("ticket-id").getAsInt();
                            for (Ticket value : map.values()) {
                                if (value.getTicketId() == ticketId) {
                                    String userId = value.getUserId();
                                    String user;
                                    if (jda.getUserById(userId) == null) {
                                        user = "User unavailable";
                                    } else {
                                        user = jda.getUserById(userId).getAsTag();
                                    }
                                    if (value.getSolved() == true) {
                                        emb.setTitle("Closed! " + ticketId)
                                                .setColor(0xff55ff)
                                                .setAuthor(user)
                                                .addField("Topic", value.getTopic(), false)
                                                .addField("Message", value.getMessage(), false)
                                                .setFooter("Time opened " + value.getTimeSubmitted()
                                                        + " \u2022 Time closed "
                                                        + OffsetDateTime.now().format(dtf));
                                        event.replyEmbeds(emb.build())
                                                .queue();
                                    } else {
                                        emb.setTitle(user)
                                                .setColor(0xff55ff)
                                                .setAuthor(ticketId.toString())
                                                .addField("Topic", value.getTopic(), false)
                                                .addField("Message", value.getMessage(), false)
                                                .setFooter("Ticket opened " + value.getTimeSubmitted());
                                        event.replyEmbeds(emb.build())
                                                .addActionRow(Button.primary("close", "close ticket"))
                                                .queue();
                                    }
                                } else {
                                    event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                                }
                            }
                        } else {
                            ArrayList<Ticket> tickets = new ArrayList<>();
                            for (Ticket value : map.values()) {
                                if (value.getSolved() == false) {
                                    tickets.add(value);
                                }
                            }
                            for (Ticket ticket : tickets) {
                                emb.addField(ticket.getTicketId().toString(), ticket.getTopic(), false);
                            }
                            emb.setTitle("Open tickets")
                                    .setColor(0xff55ff);
                            event.replyEmbeds(emb.build()).queue();
                        }
                    } catch (FileNotFoundException e) {
                        event.reply("There are no tickets avlaible").setEphemeral(true).queue();
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        event.reply("There are no tickets avlaible").setEphemeral(true).queue();
                        e.printStackTrace();
                    } catch (InvalidDefinitionException e) {
                        event.reply("There are no tickets avlaible").setEphemeral(true).queue();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.reply("You can only call this method in the "
                            + event.getGuild().getTextChannelById("1122870579809243196").getAsMention() + " channel")
                            .setEphemeral(true)
                            .queue();
                }
            } else {
                event.reply("You do not have the necessary permissions for this action").setEphemeral(true).queue();
            }
        }
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
            String ticketId = null;
            if (message.getEmbeds().size() > 0) {
                if (message.getEmbeds().get(0) != null) {
                    ticketId = message.getEmbeds().get(0).getTitle();
                } else {
                    ticketId = null;
                }
            } else {
                ticketId = null;
            }
            if (ticketId == null) {
                event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
            } else {
                System.out.println(ticketId);
                EmbedBuilder emb = new EmbedBuilder();
                ObjectMapper mapper = new ObjectMapper();
                Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                    System.out.println(map);
                    for (Ticket value : map.values()) {
                        if (value.getTicketId().toString() == ticketId) {
                            System.out.println(value);
                            value.ticketSetSolvedTrue();
                            value.ticketSetSolvedTime(OffsetDateTime.now().format(dtf));
                            String userId = value.getUserId();
                            User user;
                            String userMention;
                            if (jda.getUserById(userId) == null) {
                                userMention = "User unavailable";
                            } else {
                                user = jda.getUserById(userId);
                                userMention = user.getAsMention();
                                user.openPrivateChannel().flatMap(channel -> channel.sendMessage(user
                                        + " your support form with the ID **" + ticketId
                                        + "** has been marked as closed. The problem should be solved now. If this is not the case, please contact a support member or open a new ticket under the same ticket ID."))
                                        .queue();
                            }
                            message.editMessageEmbeds(
                                    emb.setTitle("Closed! " + ticketId)
                                            .setColor(0xff55ff)
                                            .setAuthor(userMention)
                                            .addField("Topic", value.getTopic(), false)
                                            .addField("Message", value.getMessage(), false)
                                            .setFooter("Time opened " + value.getTimeSubmitted()
                                                    + " \u2022 Time closed " + OffsetDateTime.now().format(dtf))
                                            .build())
                                    .queue();
                            event.reply(
                                    "The ticket with the ID **" + ticketId
                                            + "** has been marked as closed\n" + OffsetDateTime.now().format(dtf))
                                    .setEphemeral(true).queue();
                        } else {
                            event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("ticket")) {
            String topic = event.getValue("topic").getAsString();
            String message = event.getValue("message").getAsString();
            User user = event.getUser();
            TicketId ticketId = new TicketId(1);
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
            Map<String, TicketId> ticketIdMap = new HashMap<String, TicketId>();
            EmbedBuilder emb = new EmbedBuilder();

            try {
                ticketIdMap = mapper.readValue(new File("ticketId.json"), new TypeReference<Map<String, TicketId>>() {});
                ticketId.setTicketId(ticketIdMap.get("Ticket ID").getTicketId() + 1);
                ticketIdMap.put("Ticket ID", ticketId);
            } catch (FileNotFoundException e) {
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

            Ticket ticket = new Ticket(false, ticketId.getTicketId(), user.getId(), topic, message, OffsetDateTime.now().format(dtf), null);

            try {
                tickets = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                });
                tickets.put(ticketId.getTicketId(), ticket);
            } catch (FileNotFoundException e) {
                tickets.put(ticketId.getTicketId(), ticket);
            } catch (MismatchedInputException e) {
                tickets.put(ticketId.getTicketId(), ticket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("tickets.json"), tickets);
            } catch (IOException e) {
                e.printStackTrace();
            }

            emb.setTitle(user.getAsTag())
                .setColor(0xff55ff)
                .setAuthor(ticketId.getTicketId().toString())
                .addField("Topic", topic, false)
                .addField("Message", message, false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(dtf));

            user.openPrivateChannel().flatMap(channel -> channel.sendMessage(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed.\nYour Ticket has the ID **"
                    + ticketId.getTicketId()
                    + "**\n" + OffsetDateTime.now().format(dtf)))
                    .queue();

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed.\nYour Ticket has the ID **"
                            + ticketId.getTicketId() + "**.\nKeep this ID, a member of support might get back to you.\n"
                            + OffsetDateTime.now().format(dtf))
                    .setEphemeral(true).queue();

            event.getGuild().getTextChannelById("1122870579809243196").sendMessageEmbeds(emb.build())
                .setActionRow(Button.primary("close", "close ticket"))
                .queue();
        }
    }
}
