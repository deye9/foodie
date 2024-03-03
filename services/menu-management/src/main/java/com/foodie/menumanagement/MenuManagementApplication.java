package com.foodie.menumanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.foodie.multimodule")
public class MenuManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuManagementApplication.class, args);
	}
}