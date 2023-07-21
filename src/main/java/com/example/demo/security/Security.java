package com.example.demo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter{

	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub

		auth.jdbcAuthentication()
		.dataSource(datasource)
		
		//Following will select the username from database depending on the username from Login form
		.usersByUsernameQuery("select username,password,enabled from tbl_users where username=? ")
		
		//Following will select the authority(s) depending on the username
		.authoritiesByUsernameQuery("select username,role from tbl_users  where username=?")
		
		.passwordEncoder(new BCryptPasswordEncoder())
		;

	}
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
					.authorizeHttpRequests()
					.anyRequest()
					.authenticated()
					.and()
					.formLogin()
					.permitAll()
					.defaultSuccessUrl("/")
					.and()
					.logout()
					.invalidateHttpSession(true)
					;
	}
}
