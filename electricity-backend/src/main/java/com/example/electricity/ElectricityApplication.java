package com.example.electricity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableCaching
@EnableScheduling
public class ElectricityApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElectricityApplication.class, args);
  }

}
