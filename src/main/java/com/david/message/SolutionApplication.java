package com.david.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolutionApplication.class, args);
    }


}
