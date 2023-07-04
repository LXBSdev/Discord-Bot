package main;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Ticket implements Serializable {
    private Boolean solved;
    private Integer ticketId;
    private String userId;
    private String topic;
    private String message;
    private OffsetDateTime timeSubmitted;
    private OffsetDateTime timeClosed;

    public Ticket(Boolean lsolved, Integer lticketId, String luserId, String ltopic, String lmessage,
            OffsetDateTime ltimeSubmitted, OffsetDateTime ltimeClosed) {
        solved = lsolved;
        ticketId = lticketId;
        userId = luserId;
        topic = ltopic;
        message = lmessage;
        timeSubmitted = ltimeSubmitted;
        timeClosed = ltimeClosed;
    }

    void ticketSetSolvedTrue() {
        solved = true;
    }

    public Boolean getSolved() {
        return solved;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public String getUserId() {
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
}
