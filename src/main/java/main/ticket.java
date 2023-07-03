package main;

import java.io.Serializable;

import net.dv8tion.jda.api.entities.*;

public class ticket implements Serializable {
    private boolean solved = false;
    private int ticketId;
    private User user;
    private String topic;
    private String message;

    public ticket(boolean lsolved, int lticketId, User luser, String ltopic, String lmessage) {
        solved = lsolved;
        ticketId = lticketId;
        user = luser;
        topic = ltopic;
        message = lmessage;
    }

    void ticketSetSolvedTrue() {
        solved = true;
    }

    public boolean getSolved() {
        return solved;
    }

    public int getTicketId() {
        return ticketId;
    }

    public User getUser() {
        return user;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
