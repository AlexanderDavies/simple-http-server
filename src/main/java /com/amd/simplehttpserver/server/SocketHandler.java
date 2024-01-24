package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.handler.ClientHandler;
import com.amd.simplehttpserver.util.ThreadRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

class SocketHandler implements Runnable {

    private final HttpServer httpServer;
    private final ThreadRunner runner;
    private boolean bound;
    private boolean isStopped = false;
    private IOException bindException;


    public SocketHandler(HttpServer httpServer) {
        this.httpServer = httpServer;
        this.bound = false;
        this.runner = new ThreadRunner(httpServer.getServerConfig().getNumThreads());
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
            httpServer.getServerSocket().bind(new InetSocketAddress(httpServer.getServerConfig().getHostname(), httpServer.getServerConfig().getPort()));
            this.bound = true;
        } catch (IOException ex) {
            this.bound = false;
            this.bindException = ex;
            return;
        }

        while (!this.httpServer.getServerSocket().isClosed() && !isStopped) {
            try {
                Socket clientSocket = httpServer.getServerSocket().accept();
                clientSocket.setSoTimeout(httpServer.getServerConfig().getTimeout());

                //input stream
                InputStream inputStream = clientSocket.getInputStream();

                //output stream
                OutputStream outputStream = clientSocket.getOutputStream();

                // handle requests
                runner.execute(ClientHandler.createHandler(httpServer, inputStream, outputStream, clientSocket));

            } catch (IOException ex) {
                // TO UPDATE
                System.out.println("Error in client connection");
            }
        }
    }
}
