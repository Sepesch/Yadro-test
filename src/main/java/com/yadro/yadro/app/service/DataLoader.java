package com.yadro.yadro.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MainService service;

    @Override
    public void run(String... args) {
        if (service.isEmpty()) {
            service.loadAndSavePersons(1000);
        }
    }
}