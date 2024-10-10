package com.amd.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {

    private SocketHandler socketHandler;

    private final String DEFAULT_HOSTNAME = "127.0.0.1";

    private final int DEFAULT_PORT = 8080;

    private final int DEFAULT_NUM_THREADS = 20;

    private final int DEFAULT_TIMEOUT = 5000;

    private boolean SSL_ENABLED = false;

    public ServerSocket getServerSocket() throws IOException {

        if (SSL_ENABLED) {
            return HttpSocketFactory.createHttpsSocket();
        }

        return HttpSocketFactory.createHttpSocket();
    }

    public ServerConfig getServerConfig() {
        return new ServerConfig(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_NUM_THREADS, DEFAULT_TIMEOUT);
    }

    public void start() throws IOException {

        ServerSocket serverSocket = getServerSocket();
        ServerConfig serverConfig = getServerConfig();

        socketHandler = new SocketHandler(serverSocket, serverConfig);

        Thread thread = new Thread(socketHandler);
        thread.setDaemon(true);

        thread.start();

        while (socketHandler.getBindException() == null && !socketHandler.isBound()) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (socketHandler.getBindException() != null) {
            throw socketHandler.getBindException();
        }

    }

    public void stop() {
        this.socketHandler.setIsStopped();
    }

}
