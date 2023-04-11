package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Employee;

@Repository("emprepo")
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}
