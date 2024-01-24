package com.amd.simplehttpserver;


import com.amd.simplehttpserver.server.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class IntegrationTest {

    HttpClient client = HttpClient.newBuilder().build();

    @Test
    void startServer() {

        HttpServer httpServer = new HttpServer();
        httpServer.register(new TestController());

        try {
            httpServer.start();

            HttpRequest request = HttpRequest.newBuilder()
                    .timeout(Duration.ofSeconds(1000))
                    .uri(new URI("http://localhost:8080/test/hello"))
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofSeconds(100))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Test");

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace() + ex.getMessage());
        }


    }
}
