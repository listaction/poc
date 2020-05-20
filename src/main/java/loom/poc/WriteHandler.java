package loom.poc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class WriteHandler extends AbstractHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(WriteHandler.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        assertMethod("GET", exchange);
        Map<String, String> params = splitQuery(exchange.getRequestURI().getQuery());
        String key = params.get("key");
        String value = params.get("value");
        if (key == null || value == null){
            respondWithString(200, "Use 'key' and 'value' parameters", exchange);
            return;
        }

        Server.getData().put(key, value);

        String response = String.format("'%s' => '%s'", key, value);
        log.info(response);

        respondWithString(200, response, exchange);
    }

}
