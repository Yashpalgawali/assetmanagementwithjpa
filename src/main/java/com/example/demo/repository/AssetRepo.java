package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Assets;

@Repository("assetrepo")
public interface AssetRepo extends JpaRepository<Assets, Long> {

	
	@Query("UPDATE Assets a SET a.asset_name=:aname,a.atype=:tid,a.asset_number=:asnum,a.model_number=:monum WHERE a.asset_id=:aid")
	@Modifying
	@Transactional
	public int updateAsset(String aname,Long tid,String asnum,String monum,Long aid);
	

	@Query("SELECT a FROM Assets a JOIN a.atype  atypes")// JOIN using JPQL
	//@Query(value="SELECT * FROM tbl_assets JOIN tbl_assettype ON tbl_assettype.type_id=tbl_assets.type_id ",nativeQuery = true)
	public List<Assets> getAllAssets(); 
	
}
     