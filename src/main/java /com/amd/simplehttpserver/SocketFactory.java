package com.amd.simplehttpserver;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketFactory {

    public ServerSocket createHttpSocket () throws IOException {
        return new ServerSocket();
    }

}
