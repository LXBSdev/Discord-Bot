package main.events.tickets;

import main.ticket.Ticket;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.EventObject;

public class TicketCreatedEvent extends EventObject {
    private static Ticket ticket;
    private static User user;
    private static ModalInteractionEvent creationEvent;

    public TicketCreatedEvent(
            Object source,
            Ticket ticket,
            User user,
            ModalInteractionEvent creationEvent
    ) {
        super(source);
        this.ticket = ticket;
        this.user = user;
        this.creationEvent = creationEvent;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getUser() {
        return user;
    }

    /**
     * A method to retrieve the original event during which this {@link Ticket} was created.
     * @return {@link ModalInteractionEvent} the original event
     */
    public ModalInteractionEvent getCreationEvent() {
        return creationEvent;
    }
}
