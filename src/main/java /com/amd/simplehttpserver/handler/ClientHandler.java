package com.amd.simplehttpserver.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private ClientHandler(InputStream inputStream, OutputStream outputStream, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public static ClientHandler createHandler(InputStream inputStream, OutputStream outputStream, Socket clientSocket) {
        return new ClientHandler(inputStream, outputStream, clientSocket);
    }


    private void closeConnection() {
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.clientSocket.close();

        } catch (IOException ex) {
            //TO DO ... unable to close socket
            System.out.println("Error closing client socket");
        }
    }

    private HttpResponse routeRequest() throws ResponseException {
        // TO DO, DUMMY For TESTING

        HttpResponse response = new HttpResponse<>(this.outputStream);

        response.setStatus(HttpStatus.SUCCESS);

        return response;
    }


    @Override
    public void run() {

        try {
            while (!clientSocket.isClosed()) {

                // Build request
                HttpRequest request = new HttpRequest(this.inputStream);

                //TO DO: Route the request to the correct handler
                HttpResponse response = routeRequest();

                // Send the response
                response.send();

            }

        } catch (ResponseException ex) {
            // TO DO: build response with exception
            System.out.println(ex.getMessage());
        } catch (SocketException ex) {
            //Do Nothing: Client connection has been terminated and this is to be expected
        } catch (IOException ex) {
            // TO DO: handle stream exceptions
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }
    }
}
