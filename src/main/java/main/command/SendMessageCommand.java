package main.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class SendMessageCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("sendmessage")) return;
        User user = event.getUser();
        Channel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel();
        String messageText = Objects.requireNonNull(event.getOption("message")).getAsString();
        boolean showAuthor = Objects.requireNonNull(event.getOption("showauthor")).getAsBoolean();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (!channel.getType().equals(ChannelType.TEXT)) event.replyEmbeds(new EmbedBuilder().setDescription("You can only send a message to a text channel").setColor(Color.RED).build()).setEphemeral(true).queue();

        embedBuilder.setTitle(messageText);
        if (showAuthor) { embedBuilder.setDescription("Author:" + user.getAsMention()); }

        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(channel.getId())).sendMessageEmbeds(embedBuilder.build()).queue();
        event.replyEmbeds(new EmbedBuilder().setDescription("Message sent succesfully").build()).setEphemeral(true).queue();
    }
}
