package com.amd.simplehttpserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {

    private ExecutorService executor;

    public Runner (int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    public void execute(Handler handler) {
        executor.execute(handler);

    }

}
