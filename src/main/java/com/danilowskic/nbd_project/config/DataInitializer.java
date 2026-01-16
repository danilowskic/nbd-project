package com.danilowskic.nbd_project.config;

import com.danilowskic.nbd_project.model.AppUser;
import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.repository.AppUserRepository;
import com.danilowskic.nbd_project.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(TaskRepository taskRepository,
                               AppUserRepository appUserRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("--- START SEEDING ---");

            log.info("Flushing the database...");
            taskRepository.deleteAll();
            appUserRepository.deleteAll();

            log.info("Seeding the database...");

            AppUser user1 = new AppUser();
            user1.setUsername("s25901");
            user1.setPassword(passwordEncoder.encode("pass"));
            appUserRepository.save(user1);

            AppUser user2 = new AppUser();
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user2"));
            appUserRepository.save(user2);

            Task t1 = new Task();
            t1.setTitle("Naprawa Serwera Produkcyjnego");
            t1.setCategory("IT");
            t1.setPriority(5);
            t1.setUser("s25901");
            t1.setCompleted(false);
            t1.setAttributes(Map.of(
                    "project", "Migracja Bazy Danych",
                    "server_id", "srv-001",
                    "risk_level", "CRITICAL"
            ));

            Task t2 = new Task();
            t2.setTitle("Spotkanie z Klientem Umbrella Corp");
            t2.setCategory("BUSINESS");
            t2.setPriority(4);
            t2.setUser("user2");
            t2.setCompleted(true);
            t2.setAttributes(Map.of(
                    "client", "Umbrella Corp",
                    "location", "Sala Konferencyjna A"
            ));

            Task t3 = new Task();
            t3.setTitle("Kupić papier do drukarki");
            t3.setCategory("OFFICE");
            t3.setPriority(1);
            t3.setUser("s25901");
            t3.setCompleted(false);

            Task t4 = new Task();
            t4.setTitle("Code Review - PR z archiwizacją");
            t4.setCategory("IT");
            t4.setPriority(3);
            t4.setUser("user2");
            t4.setCompleted(false);
            t4.setAttributes(Map.of(
                    "project", "Migracja Chmury",
                    "deadline", LocalDate.now().plusDays(2).toString(),
                    "reviewer", "cezary"
            ));

            Task t5 = new Task();
            t5.setTitle("Tajne zadanie");
            t5.setCategory("ZADANIE");
            t5.setPriority(5);
            t5.setUser("user2");
            t5.setCompleted(false);

            taskRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5));

            log.info("Database seeded with 5 tasks and 2 users.");
        };
    }
}
