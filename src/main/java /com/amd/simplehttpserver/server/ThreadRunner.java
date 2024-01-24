package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.handler.ClientHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadRunner {

    private ExecutorService executor;

    public ThreadRunner(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public void execute(ClientHandler clientHandler) {
        executor.execute(clientHandler);

    }

}
