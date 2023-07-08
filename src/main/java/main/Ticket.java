package main;

import java.io.Serializable;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket implements Serializable {
    private Boolean solved;
    private Integer ticketId;
    private String userId;
    private String topic;
    private String message;
    private OffsetDateTime timeSubmitted;
    private OffsetDateTime timeClosed;

    @JsonCreator
    public Ticket (
        @JsonProperty("solved") Boolean lsolved,
        @JsonProperty("ticketId") Integer lticketId,
        @JsonProperty("userID") String luserId,
        @JsonProperty("topic") String ltopic,
        @JsonProperty("message") String lmessage,
        @JsonProperty("timeSubmitted") OffsetDateTime ltimeSubmitted,
        @JsonProperty("timeClosed") OffsetDateTime ltimeClosed) {
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

    void ticketSetSolvedTime(OffsetDateTime ltimeClosed) {
        timeClosed = ltimeClosed;
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
