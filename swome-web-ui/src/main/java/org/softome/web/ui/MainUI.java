package org.softome.web.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MainUI extends SpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(MainUI.class, args);
	}
}
