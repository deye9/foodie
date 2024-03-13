package com.foodie.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan({"com.foodie"})
@EntityScan(basePackages = {"com.foodie"})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = {"com.foodie"})
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}
}
