package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "tbl_users")
public class Users {

	@Id
	@SequenceGenerator(name = "user_seq",allocationSize = 1 , initialValue = 1)
	@GeneratedValue(generator = "user_seq",strategy = GenerationType.AUTO)
	private Long user_id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private int enabled;
	
	private String role;
	
	@Transient
	private String cnf_pass;
	
	@Transient
	private String cnf_otp;
}
