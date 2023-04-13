package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssetAssignHistory;

@Repository("assetassignhistrepo")
public interface AssetAssignHistoryRepo extends JpaRepository<AssetAssignHistory, Long> {

	
//	@Query("SELECT a,employee,assets FROM AssetAssignHistory a JOIN  a.employee employee a.assets assets  WHERE a.employee.emp_id=:empid")
//	public List<AssetAssignHistory> getAssetAssginHistByEmpId(String empid);
	
}
 