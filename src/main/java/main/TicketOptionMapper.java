package main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketOptionMapper {
    public Integer ticketId;
    public Ticket ticket;

    @JsonCreator
    public TicketOptionMapper(
            @JsonProperty("ticketId") Integer ticketId,
            @JsonProperty("Ticket") Ticket ticket) {
        this.ticketId = ticketId;
        this.ticket = ticket;
    }
}
