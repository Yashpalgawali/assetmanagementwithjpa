package com.example.demo.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssignedAssets;

@Repository("assignassetrepo")
public interface AssignedAssetsRepo extends JpaRepository<AssignedAssets, Long> {


	@Query(value="SELECT tbl_assigned_assets.*,GROUP_CONCAT(tbl_assets.asset_name ) AS assigned_assets FROM tbl_assigned_assets JOIN tbl_assets ON tbl_assets.asset_id=tbl_assigned_assets.asset_id  ",nativeQuery = true)
	public List<AssignedAssets[]> getAllAssignedAssets();
}   