package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AssetType;

@Repository("atyperepo")
public interface AssetTypeRepo extends JpaRepository<AssetType, Long> {

	
	@Query("UPDATE AssetType t SET t.type_name=?2 WHERE type_id=?1")
	@Modifying
	@Transactional
	public int updateAssetType(Long atyppeid,String type);
	
}
