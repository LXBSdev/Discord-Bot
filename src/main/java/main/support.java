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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

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

    Integer ticketId;
    JDA jda;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals("support")) {
            EmbedBuilder emb = new EmbedBuilder();
            Long lxbsId = Long.parseLong("1118108459431374898");
            Long supportId = Long.parseLong("1127962706499088424");

            emb.setTitle("Support? Sure.")
                .setColor(0xff55ff)
                .setDescription("Welcome to the support center.\nIf you have a problem or question you can submit a support Ticket.")
                .addField("Email", "support@lxbs.online", true)
                .addField("Website", "https://lxbs.online", true)
                .setFooter("LXBS Support", "https://cdn.discordapp.com/attachments/837779743486378075/1122872440872247437/logo-magenta.png");

            event.replyEmbeds(emb.build()).addActionRow(
                Button.primary("ticket", "Support ticket").withEmoji(Emoji.fromUnicode("U+1F3AB")), 
                Button.link("http://lxbs.online", "lxbs.online").withEmoji(Emoji.fromCustom("lxbs", lxbsId, false)),
                Button.link("http://lxbs.online/support", "lxbs.online/support").withEmoji(Emoji.fromCustom("support", supportId, false)))
                .setEphemeral(true).queue();
        }

        if (event.getName().equals("ticket")) {
            if (event.getMember().getRoles().toString().contains("Admin")) {
                if (event.getChannel().getId().equals("1122870579809243196") | event.getChannel().getId().equals("1059792277452623872") | event.getChannel().getId().equals("1062121062067863602") | event.getChannel().getId().equals("1125757185113198645")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
                    EmbedBuilder emb = new EmbedBuilder();
                    try {
                        map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                        if (event.getOption("ticket-id") != null) {
                            Integer ticketId = event.getOption("ticket-id").getAsInt();
                            if (map.containsKey(ticketId)) {
                                Ticket ticket = map.get(ticketId);
                                String userId = ticket.getUserId();
                                User user = event.getJDA().retrieveUserById(userId).complete();
                                if (ticket.isSolved() == true) {
                                    emb.setTitle(ticketId.toString())
                                        .setColor(0xff55ff)
                                        .setAuthor("Closed \u2022 " + user.getAsMention())
                                        .addField("Topic", ticket.getTopic(), false)
                                        .addField("Message", ticket.getMessage(), false)
                                        .setFooter("Time opened " + ticket.getTimeSubmitted()
                                            + " \u2022 Time closed "
                                            + OffsetDateTime.now().format(dtf));
                                    event.replyEmbeds(emb.build())
                                        .addActionRow(
                                            Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                                            Button.primary("reply", "reply"))
                                        .queue();
                                } else {
                                    emb.setTitle(ticketId.toString())
                                        .setColor(0xff55ff)
                                        .setAuthor(user.getAsMention())
                                        .addField("Topic", ticket.getTopic(), false)
                                        .addField("Message", ticket.getMessage(), false)
                                        .setFooter("Ticket opened " + ticket.getTimeSubmitted());
                                    event.replyEmbeds(emb.build())
                                        .addActionRow(
                                            Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                                            Button.primary("close", "close ticket"),
                                            Button.primary("reply", "reply"))
                                        .queue();
                                }
                            } else {
                                event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
                            }
                        } else {
                            ArrayList<Ticket> tickets = new ArrayList<>();
                            for (Ticket value : map.values()) {
                                if (value.isSolved() == false) {
                                    tickets.add(value);
                                }
                            }
                            for (Ticket ticket : tickets) {
                                emb.addField(ticket.getTicketId().toString(), ticket.getTopic(), false);
                            }
                            emb.setTitle("Open tickets")
                                .setColor(0xff55ff);
                            event.replyEmbeds(emb.build())
                                .addActionRow(
                                    Button.secondary("refresh", Emoji.fromUnicode("U+1F504")))
                                .queue();
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
                    event.reply("You can only call this method in the channels "
                            + event.getGuild().getTextChannelById("1122870579809243196").getAsMention() + ", " + event.getGuild().getTextChannelById("1059792277452623872").getAsMention() + ", " + event.getGuild().getTextChannelById("1062121062067863602").getAsMention() + " or " + event.getGuild().getTextChannelById("1125757185113198645").getAsMention())
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
            Integer ticketId = null;
            if (message.getEmbeds().size() > 0) {
                if (message.getEmbeds().get(0) != null) {
                    ticketId = Integer.parseInt(message.getEmbeds().get(0).getTitle());
                } else {
                    ticketId = null;
                }
            } else {
                ticketId = null;
            }
            if (ticketId != null) {
                EmbedBuilder emb = new EmbedBuilder();
                ObjectMapper mapper = new ObjectMapper();
                Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                    if (map.containsKey(ticketId)) {
                        Ticket ticket = map.get(ticketId);
                        if (ticket.isSolved() == true) {
                            event.reply("This ticket is already closed.").setEphemeral(true).queue();
                        } else {
                            ticket.ticketSetSolvedTrue();
                            ticket.ticketSetSolvedTime(OffsetDateTime.now().format(dtf));
                            map.put(ticketId, ticket);
                            String userId = ticket.getUserId();
                            User user = event.getJDA().retrieveUserById(userId).complete();
                            EmbedBuilder embUser = new EmbedBuilder();

                            embUser.setTitle("Ticket solved")
                                .setColor(0xff55ff)
                                .setDescription("Your support Ticket has been solved. The problem should be fixed now, if that shouldn't be the case please contact a member of support.")
                                .addField("Ticket ID", ticket.getTicketId().toString(), false)
                                .setFooter("Time opened " + ticket.getTimeSubmitted()
                                        + " \u2022 Time closed " + OffsetDateTime.now().format(dtf));

                            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();
                            message.editMessageEmbeds(
                                emb.setTitle(ticketId.toString())
                                    .setColor(0xff55ff)
                                    .setAuthor("Closed \u2022 " + user.getAsMention())
                                    .addField("Topic", ticket.getTopic(), false)
                                    .addField("Message", ticket.getMessage(), false)
                                    .setFooter("Time opened " + ticket.getTimeSubmitted()
                                        + " \u2022 Time closed " + OffsetDateTime.now().format(dtf))
                                    .build())
                                .queue();
                            message.editMessageComponents(
                                    ActionRow.of(
                                        Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                                        Button.primary("reply", "reply")))
                                .queue();
                            event.reply(
                                "The ticket with the ID **" + ticketId
                                + "** has been marked as closed\n" + OffsetDateTime.now().format(dtf))
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

            } else {
                event.reply("No ticket could be found with the Id").setEphemeral(true).queue();
            }
        }

        if (event.getComponentId().equals("refresh")) {
            Message message = event.getMessage();
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            EmbedBuilder emb = new EmbedBuilder();

            if (message.getEmbeds().get(0).getTitle().equals("Open tickets")) {
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                    ArrayList<Ticket> tickets = new ArrayList<>();
                    for (Ticket value : map.values()) {
                        if (value.isSolved() == false) {
                            tickets.add(value);
                        }
                    }
                    for (Ticket ticket : tickets) {
                        emb.addField(ticket.getTicketId().toString(), ticket.getTopic(), false);
                    }
                    emb.setTitle("Open tickets")
                        .setColor(0xff55ff);

                    event.deferEdit().setEmbeds(emb.build()).setActionRow(Button.secondary("refresh", Emoji.fromUnicode("U+1F504"))).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Integer ticketId = Integer.parseInt(message.getEmbeds().get(0).getTitle());
                try {
                    map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {});
                    Ticket ticket = map.get(ticketId);
                    User user = event.getJDA().retrieveUserById(ticket.getUserId()).complete();
                    if (ticket.isSolved() == true) {
                        event.deferEdit().setEmbeds(
                            emb.setTitle(ticketId.toString())
                                .setColor(0xff55ff)
                                .setAuthor("Closed \u2022 " + user.getAsMention())
                                .addField("Topic", ticket.getTopic(), false)
                                .addField("Message", ticket.getMessage(), false)
                                .setFooter("Time opened " + ticket.getTimeSubmitted()
                                    + " \u2022 Time closed "
                                    + OffsetDateTime.now().format(dtf))
                                .build())
                        .setActionRow(Button.secondary("refresh", Emoji.fromUnicode("U+1F504")), Button.primary("reply", "reply")).queue();
                    } else {
                        event.deferEdit().setEmbeds(
                            emb.setTitle(ticketId.toString())
                                .setColor(0xff55ff)
                                .setAuthor(user.getAsMention())
                                .addField("Topic", ticket.getTopic(), false)
                                .addField("Message", ticket.getMessage(), false)
                                .setFooter("Ticket opened " + ticket.getTimeSubmitted())
                                .build())
                        .setActionRow(Button.secondary("refresh", Emoji.fromUnicode("U+1F504")), Button.primary("close", "close ticket"), Button.primary("reply", "reply")).queue();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (event.getComponentId().equals("reply")) {
            Message messageOG = event.getMessage();
            String user = messageOG.getEmbeds().get(0).getAuthor().getName();
            String topic = messageOG.getEmbeds().get(0).getFields().get(0).getValue();
            String ticketId = messageOG.getEmbeds().get(0).getTitle();
            
            TextInput message = TextInput.create("message", ticketId + " \u2022 Topic: " + topic + " \u2022 Your Message:", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Your message")
                    .setMinLength(1)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("ticket", "Reply to " + user)
                    .addActionRows(ActionRow.of(message))
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
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
            Map<String, TicketId> ticketIdMap = new HashMap<String, TicketId>();
            EmbedBuilder emb = new EmbedBuilder();
            EmbedBuilder embUser = new EmbedBuilder();

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

            emb.setTitle(ticketId.getTicketId().toString())
                .setColor(0xff55ff)
                .setAuthor(user.getName())
                .addField("Topic", topic, false)
                .addField("Message", message, false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(dtf));
            
            embUser.setTitle("Ticket submitted")
                .setColor(0xff55ff)
                .setDescription("Your support Ticket has been submitted. Keep your Ticket ID, a member of support might get back to you. You will also be asked for it if you have any further questions to your ticket. You will be informed when the ticket is closed.")
                .addField("Ticket ID", ticketId.getTicketId().toString(), false)
                .addField("Topic", topic, false)
                .addField("Message", message, false)
                .setFooter("Ticket opened " + OffsetDateTime.now().format(dtf));

            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embUser.build())).queue();

            event.replyEmbeds(embUser.build()).setEphemeral(true).queue();

            event.getGuild().getTextChannelById("1122870579809243196").sendMessageEmbeds(emb.build())
                .setActionRow(
                    Button.secondary("refresh", Emoji.fromUnicode("U+1F504")),
                    Button.primary("close", "close ticket"),
                    Button.primary("reply", "reply"))
                .queue();
        }

        if (event.getModalId().equals("reply")) {
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, TicketReply> ticketsReplies = new HashMap<Integer, TicketReply>();
            Map<Integer, TicketReply> ticketReplyIdMap = new HashMap<Integer, TicketReply>();
            Integer ticketReplyId;

            try {
                ticketReplyIdMap = mapper.readValue(new File(ticketReplyId + ".json"), new TypeReference<Map<String, TicketReplyId>>() {});
                ticketId.setTicketId(ticketIdMap.get("Ticket ID").getTicketId() + 1);
                ticketIdMap.put("Ticket ID", ticketId);
            } catch (FileNotFoundException e) {
                ticketId.setTicketId(1);
                ticketIdMap.put("Ticket ID", ticketId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
