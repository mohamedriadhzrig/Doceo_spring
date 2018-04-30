package com.sopra;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sopra.core.utility.StorageService;

@SpringBootApplication
public class DoceoSpringApplication implements CommandLineRunner {

	@Resource
	StorageService storageService;
	/*
	 * spring.datasource.url=jdbc:mysql://localhost:3306/platforme
	 * spring.datasource.username=root spring.datasource.password=
	 * spring.datasource.dbcp2.driver-class-name=com.mysql.jdbc.Driver
	 * spring.jpa.hibernate.ddl-auto=update
	 * spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
	 * server.port=8080 spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS =
	 * false
	 */

	public static void main(String[] args) {
		SpringApplication.run(DoceoSpringApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... arg0) throws Exception {
		// storageService.deleteAll();
		// storageService.init();
	}
}
