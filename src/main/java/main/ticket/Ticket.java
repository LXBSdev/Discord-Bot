package main.ticket;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.events.tickets.TicketCreatedEvent;
import main.events.tickets.TicketCreatedListener;
import main.events.tickets.TicketSolvedEvent;
import main.events.tickets.TicketSolvedListener;
import main.jda.JDA;

public class Ticket implements Serializable {
    private Boolean solved;
    private final Integer ticketId;
    private final List<String> userId;
    private final String topic;
    private final String message;
    private final OffsetDateTime timeSubmitted;
    private OffsetDateTime timeClosed;
    private Duration timeWorkedOn;
    private List<TicketCreatedListener> ticketCreatedListeners = new ArrayList<>();
    private List<TicketSolvedListener> ticketSolvedListeners = new ArrayList<>();

    @JsonCreator
    public Ticket(
            @JsonProperty("solved") Boolean solved,
            @JsonProperty("ticketId") Integer ticketId,
            @JsonProperty("userId") List<String> userId,
            @JsonProperty("topic") String topic,
            @JsonProperty("message") String message,
            @JsonProperty("timeSubmitted") OffsetDateTime timeSubmitted,
            @JsonProperty("timeClosed") OffsetDateTime timeClosed,
            @JsonProperty("timeWorkedOn") Duration timeWorkedOn
    ) {
        this.solved = solved;
        this.ticketId = ticketId;
        this.userId = userId;
        this.topic = topic;
        this.message = message;
        this.timeSubmitted = timeSubmitted;
        this.timeClosed = timeClosed;
        this.timeWorkedOn = timeWorkedOn;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
        if(this.solved) ticketSolvedEvent(new TicketSolvedEvent(this, this));
    }

    public void setSolvedTime(OffsetDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }

    public void setTimeWorkedOn(Duration timeWorkedOn) {
        this.timeWorkedOn = timeWorkedOn;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimeSubmitted() {
        return timeSubmitted;
    }

    public OffsetDateTime getTimeClosed() {
        return timeClosed;
    }

    public Duration getTimeWorkedOn() {
        return timeWorkedOn;
    }

    public Boolean isSolved() {
        return solved;
    }

    public void addUserId(String userId) {
        this.userId.add(userId);
    }

    public void addTicketCreatedListener(TicketCreatedListener listener) {
        ticketCreatedListeners.add(listener);
    }

    public void removeTicketCreatedListener(TicketCreatedListener listener) {
        ticketCreatedListeners.remove(listener);
    }

    public void ticketCreatedEvent(TicketCreatedEvent event) {
        for (TicketCreatedListener listener : ticketCreatedListeners) {
            listener.ticketCreated(event);
        }
    }

    public void addTicketSolvedListener(TicketSolvedListener listener) {
        ticketSolvedListeners.add(listener);
    }

    public void removeTicketSolvedListener(TicketSolvedListener listener) {
        ticketSolvedListeners.remove(listener);
    }

    public void ticketSolvedEvent(TicketSolvedEvent event) {
        for(TicketSolvedListener listener : ticketSolvedListeners) {
            listener.ticketSolved(event);
        }
    }
}
