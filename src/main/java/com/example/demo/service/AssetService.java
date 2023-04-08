package com.example.demo.service;

import java.util.List;

import com.example.demo.models.Assets;

public interface AssetService {

	
	public Assets saveAssets(Assets asset);
	
	public List<Assets> getAllAssets();
	
	public Assets getAssetsById(String id);
	
	public int updateAssets(Assets asset);	
	
}
