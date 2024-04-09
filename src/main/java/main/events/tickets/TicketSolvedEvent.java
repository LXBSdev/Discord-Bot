package main.events.tickets;

import main.ticket.Ticket;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.EventObject;

public class TicketSolvedEvent extends EventObject {
    private static Ticket ticket;

    public TicketSolvedEvent(
            Object source,
            Ticket ticket
    ) {
        super(source);
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
