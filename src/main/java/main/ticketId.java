package main;

public class ticketId {
    private int ticketId;

    public ticketId(int lticketId) {
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
