package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AssignedAssets;
import com.example.demo.repository.AssignedAssetsRepo;

@Service("assignassetserv")
public class AssignedAssetServImpl implements AssignedAssetService {

	@Autowired
	AssignedAssetsRepo assignassetrepo;
	
	@Override
	public AssignedAssets saveAssignedAssets(AssignedAssets assign) {
		// TODO Auto-generated method stub
		return assignassetrepo.save(assign);
	}

	@Override
	public List<AssignedAssets> getAllAssignedAssets() {
		// TODO Auto-generated method stub
		return assignassetrepo.getAllAssignedAssets();
	}

}
