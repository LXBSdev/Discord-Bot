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
        @JsonProperty("replyId") Integer lreplyId,
        @JsonProperty("userId") String luserId,
        @JsonProperty("topic") String ltopic,
        @JsonProperty("message") String lmessage,
        @JsonProperty("timeSubmitted") String ltimeSubmitted
    ) {
        replyId = lreplyId;
        userId = luserId;
        topic = ltopic;
        message = lmessage;
        timeSubmitted = ltimeSubmitted;

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
