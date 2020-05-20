package loom.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestClient {

    private static final Logger log = LoggerFactory.getLogger(TestClient.class);
    private static final String baseUrl = "http://localhost:9090";

    public static void main(String[] args) {

        ThreadFactory factory = Thread.builder().virtual().name("client", 0).factory();
        Executor executor = Executors.newUnboundedExecutor(factory);

        HttpClient client = HttpClient.newHttpClient();

        for (int i=0;i<1000000;i++){
            final int key = i;
            String randomValue = UUID.randomUUID().toString();
            executor.execute(()->writeValue(client, String.valueOf(key), randomValue));
            executor.execute(()->readValue(client, String.valueOf(key)));
        }

    }

    private static void writeValue(HttpClient client, String key, String value) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/write?key=%s&value=%s", baseUrl, key, value)))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> log.info("write value: {}", response))
                .join();
    }

    private static void readValue(HttpClient client, String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/read?key=%s", baseUrl, key)))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> log.info("read value: {}", response))
                .join();
    }
}
