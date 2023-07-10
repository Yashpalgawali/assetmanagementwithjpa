package com.example.demo;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@SpringBootApplication
public class NewassetmanagementWithJpaApplication {

	@Autowired
	static
	HttpSession sess;
	
	@Autowired
	static
	HttpServletRequest request;
	
	@Autowired
	static
	Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(NewassetmanagementWithJpaApplication.class, args);
				
		String base_url =	ServletUriComponentsBuilder
				.fromRequestUri(request)
				.replacePath(null)
				.build()
				.toUriString();
		
		sess.setAttribute("base_url", base_url);
		sess.setAttribute("appname", env.getProperty("spring.application.name"));
	}
}