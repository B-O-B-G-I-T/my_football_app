package com.match_service.match_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchServiceApplication.class, args);
	}

}
