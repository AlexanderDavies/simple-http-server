package com.amd.simplehttpserver;

public class ServerConfig {

    private String hostname;
    private int port;
    private int timeout;
    private int numThreads;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public ServerConfig(String hostname, int port, int numThreads) {
        this.hostname = hostname;
        this.port = port;
        this.numThreads = numThreads;
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
}
