package com.github.yongchristophertang.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
public class DemoApplication {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    protected final void runDemoService() throws InterruptedException, ExecutionException, TimeoutException {
        executor.submit(() -> SpringApplication.run(DemoApplication.class).getStartupDate()).get(30, TimeUnit.SECONDS);
    }

    protected final void stopDemoService() {
        executor.shutdownNow();
    }
}
