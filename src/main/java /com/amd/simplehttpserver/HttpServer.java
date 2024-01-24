package com.amd.simplehttpserver;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {
    //decode params, path variables, body, headers

    private RunnableServer runnableServer;

    private String DEFAULT_HOSTNAME = "";

    private int DEFAULT_PORT = 8080;

    private int DEFAULT_NUM_THREADS = 20;

    private int DEFAULT_TIMEOUT = 10000;

    public void main(String... args) {
        try {
            start();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public ServerSocket getServerSocket() throws IOException {
        SocketFactory socketFactory = new SocketFactory();
        return socketFactory.createHttpSocket();
    }

    public ServerConfig getServerConfig() {
        return new ServerConfig(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_NUM_THREADS);
    }

    public void start() throws IOException {

        ServerSocket serverSocket = getServerSocket();
        ServerConfig serverConfig = getServerConfig();

        runnableServer  = new RunnableServer(serverSocket, serverConfig);

        //need to assign to thread Daemon.
        Thread thread = new Thread(runnableServer);
        thread.setDaemon(true);

        thread.start();

        while(runnableServer.getBindException() == null && !runnableServer.isBound()) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(runnableServer.getBindException() != null) {
            throw runnableServer.getBindException();
        }

    }

    public void stop() {
        this.runnableServer.setStopped(true);
    }

}
