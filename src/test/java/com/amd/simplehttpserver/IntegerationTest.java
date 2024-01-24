package com.amd.simplehttpserver;


import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IntegerationTest {

    @Test
    void startServer() {

        HttpServer httpServer = new HttpServer();

        try {
            httpServer.start();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace() + ex.getMessage());
        }


    }
}
