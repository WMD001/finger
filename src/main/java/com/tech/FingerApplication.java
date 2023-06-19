package com.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class FingerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FingerApplication.class, args);
    }

}
