package com.pequla.data.runner;

import com.pequla.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncRunner implements CommandLineRunner {

    private final DataService service;

    @Override
    public void run(String... args) throws Exception {
        log.info("Started automatic synchronisation");
        Timer timer = new Timer("TimedAutoSync");

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    service.syncDeleted();
                    service.syncCreated();

                } catch (IOException | InterruptedException e) {
                    log.error("Failed to retrieve updates", e);
                }
            }
        }, 0, 300000);
    }
}
