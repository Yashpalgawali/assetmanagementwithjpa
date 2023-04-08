package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_company")
public class Company {

	
	@Id
	@SequenceGenerator(name = "comp_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "comp_seq")
	private Long comp_id;
	
	private String comp_name;
	
}
