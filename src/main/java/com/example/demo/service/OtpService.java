package com.example.demo.service;

public interface OtpService {

	int generateotp(String uname);
	
	int getOtp(String uname);
	
	void clearOtp(String uname);
	
}
