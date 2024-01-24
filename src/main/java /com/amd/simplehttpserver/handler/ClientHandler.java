package com.amd.simplehttpserver.handler;

import com.amd.simplehttpserver.server.HttpServer;
import com.amd.simplehttpserver.server.Route;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

  private final Socket clientSocket;
  private final InputStream inputStream;
  private final OutputStream outputStream;
  private final HttpServer httpServer;

  private ClientHandler(HttpServer httpServer, InputStream inputStream, OutputStream outputStream, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.inputStream = inputStream;
    this.outputStream = outputStream;
    this.httpServer = httpServer;
  }

  public static ClientHandler createHandler(HttpServer httpServer, InputStream inputStream, OutputStream outputStream, Socket clientSocket) {
    return new ClientHandler(httpServer, inputStream, outputStream, clientSocket);
  }

  private <T> HttpResponse<?> routeRequest(HttpRequest<T> request) throws Exception {

    Route route = httpServer.getRoute(request.getPath(), request.getMethod());

    Object body = route.execute(request);

    // TO DO, DUMMY For TESTING
    HttpResponse<?> response = new HttpResponse<>(body, outputStream);

    response.setStatus(HttpStatus.SUCCESS);

    return response;
  }


  @Override
  public void run() {

    try (clientSocket; inputStream; outputStream) {
      while (!clientSocket.isClosed()) {

        // Build request
        HttpRequest<?> request = new HttpRequestImpl<>(this.inputStream).build();

        //TO DO: Route the request to the correct handler
        HttpResponse<?> response = routeRequest(request);

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
    } catch (Exception ex) {
      // TO DO: handle other exceptions
      System.out.println("Error processing request: " + ex.getMessage());
    }
  }
}
