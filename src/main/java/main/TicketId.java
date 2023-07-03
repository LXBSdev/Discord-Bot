package main;

import java.io.Serializable;

public class TicketId implements Serializable {
    private int ticketId;

    public TicketId(int lticketId) {
        ticketId = lticketId;
    }

    public void setTicketId(int lticketId) {
        if (lticketId > 0) {
            ticketId = lticketId;
        }
    }

    public int getTicketId() {
        return ticketId;
    }
}
