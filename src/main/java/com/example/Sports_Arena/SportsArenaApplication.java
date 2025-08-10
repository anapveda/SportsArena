package com.example.Sports_Arena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class SportsArenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsArenaApplication.class, args);
	}

}
