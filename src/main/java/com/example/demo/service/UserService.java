package com.example.demo.service;

import com.example.demo.models.Users;

public interface UserService {

	public int updateUsersPassword(String pass,Long id);
	
	public Users getUserByEmailId(String email);

	public Users getUserByUserName(String uname);

	public Users getUserByUserId(Long uid);

}
