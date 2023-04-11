package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssignedAssets;

@Repository("assignassetrepo")
public interface AssignedAssetsRepo extends JpaRepository<AssignedAssets, Long> {

	
	@Query("SELECT a FROM AssignedAssets a JOIN a.employee employee JOIN a.asset asset")
	public List<AssignedAssets> getAllAssignedAssets();
}
