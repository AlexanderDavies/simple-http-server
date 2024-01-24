package com.amd.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpSocketFactory {

    public static ServerSocket createHttpSocket () throws IOException {
        return new ServerSocket();
    }

    public static ServerSocket createHttpsSocket () throws IOException {
        // TO DO
        return new ServerSocket();
    }

}
