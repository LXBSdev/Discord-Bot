package main;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Feedback implements Serializable {
    private Integer feedbackId;
    private String userId;
    private String message;
    private String timeSubmitted;

    @JsonCreator
    public Feedback (
        @JsonProperty("ticketId") Integer lfeedbackId,
        @JsonProperty("userID") String luserId,
        @JsonProperty("message") String lmessage,
        @JsonProperty("timeSubmitted") String ltimeSubmitted) {
            feedbackId = lfeedbackId;
            userId = luserId;
            message = lmessage;
            timeSubmitted = ltimeSubmitted;
    }

    public Integer getFeedbakId() {
        return feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }
}
