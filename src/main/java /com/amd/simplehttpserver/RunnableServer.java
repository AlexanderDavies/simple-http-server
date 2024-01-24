package com.amd.simplehttpserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RunnableServer implements Runnable {

    private ServerSocket serverSocket;
    private ServerConfig serverConfig;
    private boolean bound;
    private IOException bindException;
    private boolean stopped = false;
    private Runner runner;

    public RunnableServer(ServerSocket serverSocket, ServerConfig serverConfig) {
        this.serverSocket = serverSocket;
        this.serverConfig = serverConfig;
        this.bound = false;
        this.runner = new Runner(serverConfig.getNumThreads());
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isBound() {
        return bound;
    }

    public IOException getBindException() {
        return bindException;
    }

    public void run() {
        try {
            serverSocket.bind(new InetSocketAddress(serverConfig.getHostname(), serverConfig.getPort()));
            this.bound = true;
        } catch (IOException ex) {
            this.bindException = ex;
            return;
        }

        while (!stopped) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(serverConfig.getTimeout());

                //input stream
                InputStream inputStream = socket.getInputStream();

                // handle requests



            } catch (IOException ex) {
                // need to do something here to notify client connection is broken
            }
        }


    }
}
