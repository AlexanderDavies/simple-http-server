package com.amd.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpSocketFactory {

    public ServerSocket createHttpSocket () throws IOException {
        return new ServerSocket();
    }

}
