package com.example.demo.service;

import java.util.List;

import com.example.demo.models.AssetAssignHistory;

public interface AssetAssignHistService {

	public AssetAssignHistory saveAssetAssignHistory(AssetAssignHistory ahist);
	
	public List<AssetAssignHistory> getAssetAssignHistoryByEmpId(String empid);
	
	
	
}
