package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class NewassetmanagementWithJpaApplication  {

	public static void main(String[] args) {
		SpringApplication.run(NewassetmanagementWithJpaApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder passEncode(){
		return new BCryptPasswordEncoder();  
	}
}