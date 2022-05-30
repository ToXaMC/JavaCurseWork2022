package ru.mirea;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MireaApplication {

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		Application.launch(Start.class);
	}

}