package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

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
            if (event.getMember().getRoles().contains("Admin")) {
                if (event.getChannel().getId().equals("1122870579809243196")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
                    EmbedBuilder emb = new EmbedBuilder();
                    try {
                        map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                        });
                        if (event.getOption("ticket-id") != null) {
                            Integer ticketId = event.getOption("ticket-id").getAsInt();
                            for (Ticket value : map.values()) {
                                if (value.getTicketId() == ticketId) {
                                    String userId = value.getUserId();
                                    String user;
                                    if (jda.getUserById(userId) == null) {
                                        user = "User unavailable";
                                    } else {
                                        user = jda.getUserById(userId).getAsMention();
                                    }
                                    emb.setTitle("Ticket: " + ticketId)
                                            .setColor(0xff55ff)
                                            .setAuthor(user)
                                            .addField("Topic", value.getTopic(), false)
                                            .addField("Message", value.getMessage(), false);
                                    event.replyEmbeds(emb.build())
                                            .addActionRow(Button.primary("close", "close ticket"))
                                            .queue();
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
                    } catch (ClassCastException e) {
                        event.reply("There are no tickets avlaible").setEphemeral(true).queue();
                    } catch (InvalidDefinitionException e) {
                        event.reply("There are no tickets avlaible").setEphemeral(true).queue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.reply("You can only call this method in the #tickets channel").setEphemeral(true).queue();
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

        if (event.getComponentId().equals("close")) {
            Message message = event.getMessage();
            String[] messageRaw = message.getContentRaw().split(" ");
            String ticketId = messageRaw[2];
            EmbedBuilder emb = new EmbedBuilder();
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                });
                for (Ticket value : map.values()) {
                    if (value.getTicketId().toString() == ticketId) {
                        value.ticketSetSolvedTrue();
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
                                emb.setTitle("Ticket: " + ticketId)
                                        .setColor(0xff55ff)
                                        .setAuthor(userMention)
                                        .addField("Topic", value.getTopic(), false)
                                        .addField("Message", value.getMessage(), false)
                                        .setFooter("Ticket closed")
                                        .build())
                                .queue();
                        event.reply(
                                "The ticket with the ID **" + ticketId
                                        + "** has been marked as closed")
                                .setEphemeral(true).queue();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("ticket")) {
            String topic = event.getValue("topic").getAsString();
            String message = event.getValue("message").getAsString();
            User user = event.getUser();
            Integer lticketId = 0;
            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, Ticket> map = new HashMap<Integer, Ticket>();
            if (ticketId == null) {
                lticketId = 1;
                ticketId = 1;
            } else {
                ticketId++;
                lticketId = ticketId;
            }
            Ticket ticket = new Ticket(false, lticketId, user.getId(), topic, message);

            try {
                map = mapper.readValue(new File("tickets.json"), new TypeReference<Map<Integer, Ticket>>() {
                });
                System.out.println(map);
                map.put(lticketId, ticket);
                System.out.println(map);
            } catch (FileNotFoundException e) {
                map.put(lticketId, ticket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mapper.writeValue(new File("tickets.json"), map);
                System.out.println(map);
            } catch (IOException e) {
                e.printStackTrace();
            }

            event.reply(
                    "Your Support Form has been submited. You'll be informed when your Form has been processed.\nYour Ticket has the ID **"
                            + lticketId + "**.\nKeep this ID, a member of support might get back to you.")
                    .setEphemeral(true).queue();
        }
    }
}
