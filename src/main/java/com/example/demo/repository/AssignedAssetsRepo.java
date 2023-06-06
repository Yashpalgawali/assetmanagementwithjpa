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


	@Query("SELECT a FROM AssignedAssets a")
	public List<AssignedAssets> getAllAssignedAssets();
	

	@Query(value="SELECT * FROM tbl_assigned_assets JOIN tbl_employee ON tbl_employee.emp_id=tbl_assigned_assets.emp_id where tbl_assigned_assets.emp_id=:eid",nativeQuery = true)
	public List<AssignedAssets> getAllAssignedAssetsByEmpId(Long eid);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM AssignedAssets a  WHERE a.asset.asset_id=:assetid AND a.employee.emp_id=:empid")
	public int deleteAssignedAssetByEmpidAssetId(Long assetid,Long empid);

	/*@Query(value="SELECT tas.*,ta.*,at.*,te.*,des.*,td.*,tc.*,"
			+ "GROUP_CONCAT(ta.asset_name) AS assigned_assets,"
			+ "GROUP_CONCAT(at.type_name) AS assigned_asset_types FROM tbl_assigned_assets  AS tas "
			+ "JOIN tbl_assets AS ta ON ta.asset_id=tas.asset_id  "
			+ "JOIN tbl_employee AS te ON te.emp_id=tas.emp_id "
			+ "JOIN tbl_assettype AS at ON at.type_id=ta.type_id "
			+ "JOIN tbl_designation AS des ON des.desig_id=te.desig_id "
			+ "JOIN tbl_department AS td ON td.dept_id=te.dept_id "
			+ "JOIN tbl_company AS tc ON tc.comp_id=td.comp_id "
			+ "GROUP BY tas.emp_id",nativeQuery = true)*/
	//@Query(value="SELECT tbl_assigned_assets.*,tbl_assets.*,tbl_assettype.*,tbl_employee.*,tbl_designation.*,tbl_department.*,tbl_company.*,GROUP_CONCAT(tbl_assets.asset_name) AS assigned_assets,GROUP_CONCAT(tbl_assettype.type_name) AS assigned_asset_types FROM tbl_assigned_assets JOIN tbl_assets ON tbl_assets.asset_id=tbl_assigned_assets.asset_id  JOIN tbl_employee ON tbl_employee.emp_id=tbl_assigned_assets.emp_id JOIN tbl_assettype ON tbl_assettype.type_id=tbl_assets.type_id JOIN tbl_designation ON tbl_designation.desig_id=tbl_employee.desig_id JOIN tbl_department ON tbl_department.dept_id=tbl_employee.dept_id JOIN tbl_company ON tbl_company.comp_id=tbl_department.comp_id GROUP BY tbl_assigned_assets.emp_id",nativeQuery = true)
	
	@Query(value="SELECT tas.*,GROUP_CONCAT(ta.asset_name),GROUP_CONCAT(at.type_name),te.emp_name,te.emp_email,te.emp_contact,te.desig_id,des.desig_name,td.dept_id,td.dept_name,td.comp_id,tc.comp_name,GROUP_CONCAT(ta.model_number) FROM tbl_assigned_assets tas JOIN tbl_assets ta ON ta.asset_id=tas.asset_id JOIN tbl_assettype AS at ON at.type_id=ta.type_id JOIN tbl_employee AS te ON te.emp_id=tas.emp_id JOIN tbl_designation AS des ON des.desig_id=te.desig_id JOIN tbl_department AS td ON td.dept_id=te.dept_id JOIN tbl_company AS tc ON tc.comp_id=td.comp_id GROUP BY te.emp_id",nativeQuery = true)
	public List<Object[]> getAllNewAssignedAssets();
	//public List<Object[]> getAllNewAssignedAssets();
	
	
	
}   