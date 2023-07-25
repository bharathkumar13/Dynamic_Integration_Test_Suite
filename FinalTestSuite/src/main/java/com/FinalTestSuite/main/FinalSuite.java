package com.FinalTestSuite.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FinalSuite {

	public static void main(String[] args) {
		SpringApplication.run(FinalSuite.class, args);
	}

}
