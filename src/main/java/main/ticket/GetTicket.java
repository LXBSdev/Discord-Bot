package main.ticket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GetTicket {

    private static Map<Integer, Ticket> map;
    private static ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    public static Map<Integer, Ticket> getTicketMap() {
        try {
            map = mapper.readValue(new File("tickets.json"), new TypeReference<>() {});
            return map;
        } catch (ClassCastException | IOException e) {
            return null;
        }
    }

    public static Ticket getTicketById(Integer ticketId) {
        map = getTicketMap();
        if (map.get(ticketId) == null) {
            return null;
        } else {
            return map.get(ticketId);
        }
    }
}
