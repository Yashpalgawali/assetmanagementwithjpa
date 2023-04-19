package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssignedAssets;

@Repository("assignassetrepo")
public interface AssignedAssetsRepo extends JpaRepository<AssignedAssets, Long> {

	
	//@Query("SELECT a FROM AssignedAssets a JOIN a.employee employee JOIN a.asset asset")
	@Query(value="SELECT *,GROUP_CONCAT(tbl_assets.asset_name) assigned_assets FROM tbl_assigned_assets JOIN tbl_employee ON tbl_employee.emp_id=tbl_assigned_assets.emp_id JOIN tbl_assets ON tbl_assets.asset_id=tbl_assigned_assets.asset_id JOIN tbl_assettype ON tbl_assettype.type_id=tbl_assets.type_id GROUP BY tbl_assigned_assets.emp_id",nativeQuery = true)
	public List<AssignedAssets> getAllAssignedAssets();
}
 