package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AssetType;
import com.example.demo.repository.AssetTypeRepo;

@Service("atypeserv")
public class AssetTypeServImpl implements AssetTypeService {

	@Autowired
	AssetTypeRepo atyperepo;
	
	@Override
	public AssetType saveAssetType(AssetType atype) {
		// TODO Auto-generated method stub
		return atyperepo.save(atype);
	}

	@Override
	public List<AssetType> getAllAssetTypes() {
		// TODO Auto-generated method stub
		return atyperepo.findAll();
	}

	@Override
	public AssetType getAssetTypeById(String id) {
		// TODO Auto-generated method stub
		Long tid = Long.valueOf(id);
		
		AssetType atype = atyperepo.findById(tid).get();
	
		if(atype!=null)
		{
			return atype;
		}
		else {
			return atype;
		}
	}

	@Override
	public int updateAssetType(AssetType atype) {
		// TODO Auto-generated method stub
		return atyperepo.updateAssetType(atype.getType_id(), atype.getType_name());
	}

}
