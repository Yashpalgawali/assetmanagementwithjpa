package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Company;

@Repository("comprepo")
public interface CompanyRepo extends JpaRepository<Company, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE Company c SET c.comp_name=:cname WHERE c.comp_id=:cid")
	public int updateCompany(String cname,Long  cid);
}
