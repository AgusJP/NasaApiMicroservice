package com.prueba.apinasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ApinasaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApinasaApplication.class, args);
	}

}
