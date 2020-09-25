package loom.poc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.dto.Message;
import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Map;

public class WriteHandler extends AbstractHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(WriteHandler.class);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        assertMethod("POST", exchange);

        Message message = null;
        try {
            message = mapper.readValue(exchange.getRequestBody().readAllBytes(), Message.class);
            RTopic topic = Server.getRedissonClient().getTopic(makeTopic(message));
            topic.publish(message);
            log.debug(message.toString());
            exchange.sendResponseHeaders(204, -1);

        } catch ( Exception e) {
            e.printStackTrace();
            respondWithString(500, "internal error", exchange);
        }
    }

    private String makeTopic(Message message) {
        return message.getUserId()+"."+ message.getDeviceId();
    }

}
