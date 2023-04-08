package com.example.demo.service;

import java.util.List;

import com.example.demo.models.AssetType;

public interface AssetTypeService {

	
	public AssetType saveAssetType(AssetType atype);
	
	public List<AssetType> getAllAssetTypes();
	
	public AssetType getAssetTypeById(String id);
	
	public int updateAssetType(AssetType atype);
	
}
