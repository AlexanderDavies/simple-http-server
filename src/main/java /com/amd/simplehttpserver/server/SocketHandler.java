package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.handler.ClientHandler;
import com.amd.simplehttpserver.util.ThreadRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class SocketHandler implements Runnable {

    private final ServerSocket serverSocket;
    private final ServerConfig serverConfig;
    private final ThreadRunner runner;
    private boolean bound;
    private boolean isStopped = false;
    private IOException bindException;


    public SocketHandler(ServerSocket serverSocket, ServerConfig serverConfig) {
        this.serverSocket = serverSocket;
        this.serverConfig = serverConfig;
        this.bound = false;
        this.runner = new ThreadRunner(serverConfig.getNumThreads());
    }

    protected boolean isBound() {
        return bound;
    }

    protected IOException getBindException() {
        return bindException;
    }

    protected void setIsStopped() {
        this.isStopped = true;
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

        while (!serverSocket.isClosed() && !isStopped) {
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
