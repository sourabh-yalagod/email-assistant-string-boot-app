package com.email_assistance.email_assistant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailAssistantApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EmailAssistantApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Application Running on PORT : 8080");
    }
}
