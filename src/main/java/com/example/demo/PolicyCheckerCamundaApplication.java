package com.example.demo;

import com.example.demo.entity.PolicyEntity;
import com.example.demo.entity.VehicleEntity;
import com.example.demo.repository.PolicyRepository;
import com.example.demo.repository.VehicleRepository;
import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class PolicyCheckerCamundaApplication {


	public static void main(String[] args) {
		SpringApplication.run(PolicyCheckerCamundaApplication.class, args);

	}

}
