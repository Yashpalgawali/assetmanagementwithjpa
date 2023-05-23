package com.example.demo.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_employee")
public class Employee {
	
	@Id
	@SequenceGenerator(name="emp_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "emp_seq")
	private Long emp_id;
	
	private String emp_name;
	
	private String emp_email;
	
	private String emp_contact;
	
	@ManyToOne(targetEntity = Designation.class,cascade = {CascadeType.MERGE})
	@JoinColumn(name="desig_id",referencedColumnName = "desig_id")
	private Designation designation;
	
	@ManyToOne(targetEntity = Department.class,cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name="dept_id",referencedColumnName = "dept_id")
	private Department department;
	
	@Transient
	private String multi_assets;
	
	@Transient
	private String comments;
	
	
}
