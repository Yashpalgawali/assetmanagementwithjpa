package com.example.demo.service;

import java.util.List;

import com.example.demo.models.Employee;

public interface EmployeeService {
	
	public Employee saveEmployee(Employee emp);
	
	public List<Employee> getAllEmployees();
	
	public Employee getEmployeeById(String empid);
	
	public int updateEmployee(Employee emp);

}
