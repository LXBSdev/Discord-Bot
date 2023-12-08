package main.command;

import main.ticket.GetTicket;
import main.ticket.PutTicket;
import main.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AddUserToTicketCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("addusertoticket")) {
            User user = event.getOption("user").getAsUser();
            String ticketId = event.getOption("ticketid").getAsString();

            Ticket ticket = GetTicket.getTicketById(Integer.parseInt(ticketId));
            ticket.addUserId(user.getId());
            PutTicket.putTicket(ticket);

            event.replyEmbeds(new EmbedBuilder().setDescription("User has been added").build()).setEphemeral(true).queue();
        }
    }
}
