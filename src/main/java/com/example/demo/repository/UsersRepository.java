package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Users;

@Repository("userrepo")
public interface UsersRepository extends JpaRepository<Users, Long> {

	
	@Query("UPDATE  Users u SET u.password=:pass WHERE u.user_id=:id")
	@Transactional
	@Modifying
	public int updateUsersPassword(String pass,Long id);
	
	//@Query("SELECT u FROM Users u WHERE u.email=?1")
	@Query(value="select * from tbl_users where email=?",nativeQuery = true)
	public Users getUserByEmailId(String email);
	
	@Query("SELECT u FROM Users u WHERE u.username=?1")
	public Users getUserByUserName(String uname);
}
