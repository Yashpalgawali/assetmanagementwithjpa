package com.example.demo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class NewassetmanagementWithJpaApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(NewassetmanagementWithJpaApplication.class, args);
	
			System.err.println("New asset management example");
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.err.println("Command line runner implemented");
	}
}