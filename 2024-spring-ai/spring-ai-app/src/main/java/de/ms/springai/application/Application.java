package de.ms.springai.application;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.ms.springai.configuration.ApplicationConfiguration;

@Configuration
@Import(ApplicationConfiguration.class)
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class);
	}

}
