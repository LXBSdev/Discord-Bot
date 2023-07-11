package main;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket implements Serializable {
    private Boolean solved;
    private Integer ticketId;
    private String userId;
    private String topic;
    private String message;
    private String timeSubmitted;
    private String timeClosed;

    @JsonCreator
    public Ticket (
        @JsonProperty("solved") Boolean solved,
        @JsonProperty("ticketId") Integer ticketId,
        @JsonProperty("userID") String userId,
        @JsonProperty("topic") String topic,
        @JsonProperty("message") String message,
        @JsonProperty("timeSubmitted") String timeSubmitted,
        @JsonProperty("timeClosed") String timeClosed) {
            this.solved = solved;
            this.ticketId = ticketId;
            this.userId = userId;
            this.topic = topic;
            this.message = message;
            this.timeSubmitted = timeSubmitted;
            this.timeClosed = timeClosed;
    }

    void ticketSetSolvedTrue() {
        solved = true;
    }

    void ticketSetSolvedTime(String ltimeClosed) {
        timeClosed = ltimeClosed;
    }

    public Boolean isSolved() {
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
