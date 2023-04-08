package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Department;

@Repository("deptrepo")
public interface DepartmentRepo extends JpaRepository<Department, Long> {

	
	@Query(value="UPDATE tbl_department SET dept_name=?1,comp_id=?2 WHERE dept_id=?3",nativeQuery = true)
	@Modifying
	@Transactional
	public int updateDepartmentById(String depname,Long cid,Long depid);
}
