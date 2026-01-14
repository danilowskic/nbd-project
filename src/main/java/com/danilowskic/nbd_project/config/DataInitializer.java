package com.danilowskic.nbd_project.config;

import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(TaskRepository taskRepository) {
        return args -> {
            log.debug("Deleting entries from the database...");
            taskRepository.deleteAll();

            Task t1 = new Task();
            t1.setTitle("Server fix");
            t1.setCategory("IT");
            t1.setPriority(5);
            t1.setAttributes(Map.of("project", "Migracja Chmury", "server_id", "srv-001"));

            Task t2 = new Task();
            t2.setTitle("Client meeting");
            t2.setCategory("BUSINESS");
            t2.setPriority(4);
            t2.setAttributes(Map.of("client", "Umbrella Corp", "location", "Sala A"));

            Task t3 = new Task();
            t3.setTitle("Buy paper");
            t3.setCategory("OFFICE");
            t3.setPriority(1);

            Task t4 = new Task();
            t4.setTitle("Code review");
            t4.setCategory("IT");
            t4.setPriority(3);
            t4.setAttributes(Map.of("project", "Migracja Chmury", "deadline", LocalDateTime.now().plusDays(2).toString()));

            log.debug("Seeding the database...");

            taskRepository.saveAll(
                    Arrays.asList(t1, t2, t3, t4)
            );

            log.debug("Database seeded");
        };
    }
}
