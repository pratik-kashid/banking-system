package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
        System.out.println("=====================================");
        System.out.println("🏦 Banking System Started Successfully!");
        System.out.println("📍 API URL: http://localhost:8080");
        System.out.println("📊 Visit MySQL Workbench to see data");
        System.out.println("=====================================");
    }
}