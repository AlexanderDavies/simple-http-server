package com.amd.simplehttpserver.server;

public class ServerConfig {

    private String hostname;
    private int port;
    private int timeout;
    private int numThreads;

    public ServerConfig(String hostname, int port, int numThreads) {
        this.hostname = hostname;
        this.port = port;
        this.numThreads = numThreads;
    }

    public ServerConfig(String hostname, int port, int numThreads, int timeout) {
        this.hostname = hostname;
        this.port = port;
        this.numThreads = numThreads;
        this.timeout = timeout;
    }

    public String getHostname() {
        return hostname;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
