package com.github.yongchristophertang.demo;

import com.github.yongchristophertang.demo.model.Person;
import com.github.yongchristophertang.demo.persist.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
public class DemoApplication {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static volatile boolean isServiceStarted = false;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(PersonRepository personRepository) {
        return event -> personRepository.save(new Person("Will O'Corner", "356 Henderson Pvt., Markham, ON, Canada",
            Arrays.asList("512-780-2257", "553-780-4889")));
    }

    protected synchronized final void runDemoService() throws InterruptedException, ExecutionException, TimeoutException {
        if (isServiceStarted) {
            return;
        }
        executor.submit(() -> SpringApplication.run(DemoApplication.class).getStartupDate()).get(40, TimeUnit.SECONDS);
        isServiceStarted = true;
    }

    protected final void stopDemoService() {
        executor.shutdownNow();
    }
}
