package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.handler.ClientHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler implements Runnable {

    private final ServerSocket serverSocket;
    private final ServerConfig serverConfig;
    private final ThreadRunner runner;
    private boolean bound;
    private IOException bindException;
    private boolean stopped = false;

    public SocketHandler(ServerSocket serverSocket, ServerConfig serverConfig) {
        this.serverSocket = serverSocket;
        this.serverConfig = serverConfig;
        this.bound = false;
        this.runner = new ThreadRunner(serverConfig.getNumThreads());
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
            this.serverSocket.bind(new InetSocketAddress(serverConfig.getHostname(), serverConfig.getPort()));
            this.bound = true;
        } catch (IOException ex) {
            this.bound = false;
            this.bindException = ex;
            return;
        }

        while (!serverSocket.isClosed() && !stopped) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(serverConfig.getTimeout());

                //input stream
                InputStream inputStream = clientSocket.getInputStream();

                //output stream
                OutputStream outputStream = clientSocket.getOutputStream();

                // handle requests
                runner.execute(ClientHandler.createHandler(inputStream, outputStream, clientSocket));

            } catch (IOException ex) {
                // TO UPDATE
                System.out.println("Error in client connection");
            }
        }
    }
}
