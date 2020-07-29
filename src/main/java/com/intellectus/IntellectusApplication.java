package com.intellectus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IntellectusApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntellectusApplication.class, args);
	}
}
