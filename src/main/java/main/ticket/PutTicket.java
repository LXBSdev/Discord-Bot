package main.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.collections4.Get;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PutTicket {

    private static Map<Integer, Ticket> map;
    private static ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    public static void putTicket(Ticket ticket) {
        map = GetTicket.getTicketMap();
        map.put(ticket.getTicketId(), ticket);
        try {
            mapper.writeValue(new File("tickets.json"), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
