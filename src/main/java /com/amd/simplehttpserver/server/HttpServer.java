package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.handler.HttpMethod;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;

public class HttpServer {

  private SocketHandler socketHandler;
  private ServerConfig serverConfig;
  private ServerSocket serverSocket;
  private final Routes routes = new Routes();

  public void setServerSocket(ServerConfig serverConfig) throws IOException {

    if (serverConfig.isSslEnabled()) {
      this.serverSocket = HttpSocketFactory.createHttpsSocket();
    }

    this.serverSocket = HttpSocketFactory.createHttpSocket();
  }

  public ServerSocket getServerSocket() {
    return serverSocket;
  }

  public void setServerConfig(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  public ServerConfig getServerConfig() {
    return this.serverConfig;
  }

  public void register(Object controller) {
    routes.register(controller);
  }

  public Route getRoute(String path, HttpMethod httpMethod) throws IllegalArgumentException {
    return routes.getRoute(path, httpMethod);
  }

  public void start() throws IOException {

    if (Objects.isNull(serverConfig)) {
      this.serverConfig = new ServerConfig();
    }

    setServerSocket(serverConfig);

    this.socketHandler = new SocketHandler(this);

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
