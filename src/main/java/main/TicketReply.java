package main;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketReply implements Serializable{
    private Integer replyId;
    private String userId;
    private String topic;
    private String message;
    private String timeSubmitted;

    @JsonCreator
    public TicketReply (
        @JsonProperty("replyId") Integer replyId,
        @JsonProperty("userId") String userId,
        @JsonProperty("topic") String topic,
        @JsonProperty("message") String message,
        @JsonProperty("timeSubmitted") String timeSubmitted
    ) {
        this.replyId = replyId;
        this.userId = userId;
        this.topic = topic;
        this.message = message;
        this.timeSubmitted = timeSubmitted;

    }

    public Integer getReplyId() {
        return replyId;
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

}
