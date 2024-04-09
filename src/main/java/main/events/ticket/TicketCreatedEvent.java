package main.events.ticket;

import main.ticket.Ticket;

import java.util.EventObject;

public class TicketCreatedEvent extends EventObject {
    private static Ticket ticket;

    public TicketCreatedEvent(
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
