package main;

import java.io.Serializable;
import java.time.Duration;
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
    private Duration timeWorkedOn;

    @JsonCreator
    public Ticket (
        @JsonProperty("solved") Boolean solved,
        @JsonProperty("ticketId") Integer ticketId,
        @JsonProperty("userId") String userId,
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

    void setSolvedTrue() {
        solved = true;
    }
    void setSolvedTime(OffsetDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }
    void setTimeWorkedOn(Duration timeWorkedOn) {
        this.timeWorkedOn = timeWorkedOn;
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
    public OffsetDateTime getTimeSubmitted() {
        return timeSubmitted;
    }
    public OffsetDateTime getTimeClosed() {
        return timeClosed;
    }
    public Duration getTimeWorkedOn() {
        return timeWorkedOn;
    }
}
