package com.amd.simplehttpserver.server;

import java.util.Objects;

public class ServerConfig {

    private final static String DEFAULT_HOSTNAME = "127.0.0.1";
    private final static int DEFAULT_PORT = 8080;
    private final static int DEFAULT_NUM_THREADS = 20;
    private final static int DEFAULT_TIMEOUT = 30000;
    private final static boolean DEFAULT_SSL_ENABLED = false;
    private final static int MAX_PORT = 65535;

    private final String hostname;
    private final int port;
    private final int timeout;
    private final int numThreads;
    private final boolean sslEnabled;

    private ServerConfig(String hostname, int port, int numThreads, int timeout, boolean sslEnabled) {

        if (Objects.isNull(hostname) || hostname.isEmpty()) {
            this.hostname = DEFAULT_HOSTNAME;
        } else {
            this.hostname = hostname;
        }

        if(port > MAX_PORT || port < 0) {
            throw new IllegalArgumentException(String.format("port must be less than or equal to %s.", MAX_PORT));
        }

        this.port = port;

        if(numThreads <= 0) {
            this.numThreads = DEFAULT_NUM_THREADS;
        } else {
            this.numThreads = numThreads;
        }

        if(timeout <= 0) {
            this.timeout = DEFAULT_TIMEOUT;
        } else {
            this.timeout = timeout;
        }

        this.sslEnabled = sslEnabled;

    }

    public ServerConfig() {
        this(null, DEFAULT_PORT, DEFAULT_NUM_THREADS, DEFAULT_TIMEOUT, DEFAULT_SSL_ENABLED);
    }

    public ServerConfig(String hostname, int port, int numThreads) {
        this(hostname, port, numThreads, DEFAULT_TIMEOUT, DEFAULT_SSL_ENABLED);
    }

    public ServerConfig(String hostname, int port, int numThreads, int timeout) {
        this(hostname, port, numThreads, timeout, DEFAULT_SSL_ENABLED);
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

    public boolean isSslEnabled() {
        return sslEnabled;
    }

}
