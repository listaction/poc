package loom.poc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class ReadHandler extends AbstractHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(ReadHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        assertMethod("GET", exchange);
        Map<String, String> params = splitQuery(exchange.getRequestURI().getQuery());
        String key = params.get("key");
        if (key == null){
            respondWithString(200, "Use 'key' parameter", exchange);
            return;
        }

        String value = Server.getData().get(key);
        String response = String.format("value: %s", value);
        log.info(response);

        respondWithString(200, response, exchange);
    }
}
