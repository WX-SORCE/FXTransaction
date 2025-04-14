package com.alxy.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        // System.setProperty("spring.profiles.active", "docker");
        System.setProperty("spring.profiles.active", "dev");
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
