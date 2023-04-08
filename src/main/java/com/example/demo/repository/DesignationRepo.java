package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Designation;

@Repository("desigrepo")
public interface DesignationRepo extends JpaRepository<Designation, Long> {

	
	@Query("UPDATE Designation d SET d.desig_name=?2 WHERE d.desig_id=?1")
	@Transactional
	@Modifying
	public int updateDesignation(Long did,String dname);
	
}
