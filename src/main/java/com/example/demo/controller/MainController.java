package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class MainController {

	@Autowired
	Environment env;
	
	
	@GetMapping("/")
	public String home(HttpSession sess,HttpServletRequest request)
	{
		String base_url =	ServletUriComponentsBuilder
				.fromRequestUri(request)
				.replacePath(null)
				.build()
				.toUriString();
		
		sess.setAttribute("base_url", base_url);
		sess.setAttribute("appname", env.getProperty("spring.application.name"));
		return "Home";
	}
	
}
