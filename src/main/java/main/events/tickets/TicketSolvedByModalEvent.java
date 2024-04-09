package main.events.tickets;

import main.ticket.Ticket;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.EventObject;

public class TicketSolvedByModalEvent extends EventObject {
    private static Ticket ticket;
    private static ModalInteractionEvent closingEvent;

    public TicketSolvedByModalEvent(
            Object source,
            Ticket ticket,
            ModalInteractionEvent closingEvent
    ) {
        super(source);
        this.ticket = ticket;
        this.closingEvent = closingEvent;
    }

    public Ticket getTicket() {
        return ticket;
    }

    /**
     * A method to retrieve the original event during which this {@link Ticket} was closed.
     * @return {@link ModalInteractionEvent} the original event
     */
    public ModalInteractionEvent getClosingEvent() {
        return closingEvent;
    }
}
