package main;

import java.io.Serializable;

import net.dv8tion.jda.api.entities.User;

public class Ticket implements Serializable {
    private String solved;
    private Integer ticketId;
    private String user;
    private String topic;
    private String message;

    public Ticket(Boolean lsolved, Integer lticketId, User luser, String ltopic, String lmessage) {
        solved = lsolved.toString();
        ticketId = lticketId;
        user = luser.toString();
        topic = ltopic;
        message = lmessage;
    }

    void ticketSetSolvedTrue() {
        solved = "true";
    }

    public Boolean getSolved() {
        if (solved.equals("false")) {
            return false;
        } else {
            return true;
        }
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public String getUser() {
        return user;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
