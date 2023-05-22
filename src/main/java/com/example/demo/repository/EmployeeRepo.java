package com.example.demo.repository;



import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Employee;

@Repository("emprepo")
public interface EmployeeRepo extends JpaRepository<Employee, Long> {


	@Modifying
	@Transactional
	@Query("UPDATE Employee e SET e.emp_name=:ename,e.emp_email=:email,e.emp_contact=:cont,e.department.dept_id=:depid,e.designation.desig_id=:desigid WHERE e.emp_id=:eid")
	public int updateEmployee(String ename,String email,String cont,Long depid,Long desigid,Long eid);
}
