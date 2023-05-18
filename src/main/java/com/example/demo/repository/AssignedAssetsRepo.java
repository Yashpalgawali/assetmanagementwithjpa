package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssignedAssets;

@Repository("assignassetrepo")
public interface AssignedAssetsRepo extends JpaRepository<AssignedAssets, Long> {


	//@Query(value="SELECT tbl_assigned_assets.*,GROUP_CONCAT(tbl_assets.asset_name ) AS assigned_assets FROM tbl_assigned_assets JOIN tbl_assets ON tbl_assets.asset_id=tbl_assigned_assets.asset_id  group by tbl_assigned_assets.emp_id",nativeQuery = true)
	@Query(value="SELECT * FROM tbl_assigned_assets JOIN tbl_assets ON tbl_assets.asset_id=tbl_assigned_assets.asset_id ",nativeQuery = true)
	public List<AssignedAssets> getAllAssignedAssets();
	
//	nameOnly getAllAssignedAssets();
//	
//	public static interface nameOnly
//	{
//		String get_Assigned_assets();
//	}

	@Query(value="SELECT * FROM tbl_assigned_assets JOIN tbl_employee ON tbl_employee.emp_id=tbl_assigned_assets.emp_id where tbl_assigned_assets.emp_id=:eid",nativeQuery = true)
	public List<AssignedAssets> getAllAssignedAssetsByEmpId(Long eid);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM AssignedAssets a  WHERE a.asset.asset_id=:assetid AND a.employee.emp_id=:empid")
	public int deleteAssignedAssetByEmpidAssetId(Long assetid,Long empid);
	
//	@Modifying
//	@Query("delete from Book b where b.title=:title")
	
}   