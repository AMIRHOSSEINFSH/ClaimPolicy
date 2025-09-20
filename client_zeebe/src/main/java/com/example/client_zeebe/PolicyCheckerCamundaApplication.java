package com.example.client_zeebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PolicyCheckerCamundaApplication {


	public static void main(String[] args) {
		SpringApplication.run(PolicyCheckerCamundaApplication.class, args);

	}

}
