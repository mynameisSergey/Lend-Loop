package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ShareItApp {
	static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);

	}

	public static ConfigurableApplicationContext getContext() {
		return context;
	}

	public static void stop() {
		context.close();
	}
}
