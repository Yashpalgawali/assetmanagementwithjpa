package com.example.demo.service;

import java.util.List;

import com.example.demo.models.AssignedAssets;

public interface AssignedAssetService {

	public AssignedAssets saveAssignedAssets(AssignedAssets assign);
	
	public List<AssignedAssets> getAllAssignedAssets();

	public List<AssignedAssets> getAssignedAssetsByEmpId(Long empid);

	public int retrieveAssetByEmpId(AssignedAssets assign);
	
	public List<Object[]> getAllAssignedassetsGroup();
	
}
