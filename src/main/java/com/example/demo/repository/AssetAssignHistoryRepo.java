package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssetAssignHistory;

@Repository("assetassignhistrepo")
public interface AssetAssignHistoryRepo extends JpaRepository<AssetAssignHistory, Long> {

	@Query("SELECT a FROM AssetAssignHistory a WHERE a.employee.emp_id=?1")
	public List<AssetAssignHistory> getAssetAssginHistByEmpId(Long empid);
}
 