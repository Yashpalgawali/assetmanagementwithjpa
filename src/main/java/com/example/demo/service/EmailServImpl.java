package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailserv")
public class EmailServImpl implements EmailService {

	@Autowired
	JavaMailSender mailsend;
	
	@Autowired
	Environment env;
	
	@Override
	public void sendSimpleEmail(String toemail, String body, String subject) {
		// TODO Auto-generated method stub

		String from = env.getProperty("spring.mail.username");
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(toemail);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(body);
		try {
			mailsend.send(message);
			System.out.println("mail sent success");
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("mail sent failed ");
		}
	}
}
