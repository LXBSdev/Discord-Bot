package main.events.tickets;

import main.ticket.Ticket;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.EventObject;

/**
 * This Event can be called when a {@link Ticket} is being closed or solved.<br>
 * To receive these events implement the {@link TicketSolvedListener} into your class.
 *
 * @author wyatt_was
 * @version 2024.4
 */
public class TicketSolvedEvent extends EventObject {
    private static Ticket ticket;
    private static ButtonInteractionEvent closingEvent;

    /**
     * Constructor for an {@code TicketSolvedEvent}<br>
     * This constructor has two parameters
     *
     * @param source The source of the event
     * @param ticket The {@link Ticket} that has been closed or solved
     */
    public TicketSolvedEvent(
            Object source,
            Ticket ticket
    ) {
        super(source);
        this.ticket = ticket;
    }

    /**
     * Constructor for an {@code TicketSolvedEvent}<br>
     * This constructor has three parameters
     *
     * @param source The source of the event
     * @param ticket The {@link Ticket} that has been closed or solved
     * @param closingEvent The original event from which this event has been called (Can only be an {@link ButtonInteractionEvent})
     */
    public TicketSolvedEvent(
            Object source,
            Ticket ticket,
            ButtonInteractionEvent closingEvent
    ) {
        super(source);
        this.ticket = ticket;
        this.closingEvent = closingEvent;
    }

    /**
     * A method to retrieve the {@link Ticket} that has been closed or solved.
     * @return Ticket - the {@link Ticket} that has been closed or solved
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * A method to retrieve the original event during which this {@link Ticket} was closed.
     * @return {@link ButtonInteractionEvent} the original event
     */
    public ButtonInteractionEvent getClosingEvent() {
        return closingEvent;
    }
}
