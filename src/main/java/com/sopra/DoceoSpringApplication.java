package com.sopra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sopra.services.AuthorityService;

@SpringBootApplication
@ComponentScan({"com.sopra.controller","com.sopra.utility","com.sopra.services","com.sopra.service.implementation","com.sopra.security","com.sopra.ElasticRepository"})
@EntityScan("com.sopra.entities")
@EnableJpaRepositories("com.sopra.DAO")
public class DoceoSpringApplication implements CommandLineRunner {

	

	public static void main(String[] args) {
		SpringApplication.run(DoceoSpringApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... arg0) throws Exception {
		
	}
}
