package main;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket implements Serializable {
    private Boolean solved;
    private Integer ticketId;
    private @Nonnull String userId;
    private String topic;
    private String message;
    private String timeSubmitted;
    private String timeClosed;

    @JsonCreator
    public Ticket (
        @JsonProperty("solved") Boolean lsolved,
        @JsonProperty("ticketId") Integer lticketId,
        @JsonProperty("userID") @Nonnull String luserId,
        @JsonProperty("topic") String ltopic,
        @JsonProperty("message") String lmessage,
        @JsonProperty("timeSubmitted") String ltimeSubmitted,
        @JsonProperty("timeClosed") String ltimeClosed) {
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

    void ticketSetSolvedTime(String ltimeClosed) {
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

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public String getTimeClosed() {
        return timeClosed;
    }
}
