package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Assets;
import com.example.demo.repository.AssetRepo;

@Service("assetserv")
public class AssetServImpl implements AssetService {

	@Autowired
	AssetRepo assetrepo;
	
	@Override
	public Assets saveAssets(Assets asset) {
		// TODO Auto-generated method stub
		return assetrepo.save(asset);
	}

	@Override
	public List<Assets> getAllAssets() {
		// TODO Auto-generated method stub
		return assetrepo.getAllAssets();
	}

	@Override
	public Assets getAssetsById(String id) { 
		// TODO Auto-generated method stub
		Long aid =Long.valueOf(id);
		return assetrepo.findById(aid).get();
	}

	@Override
	public int updateAssets(Assets asset) {
		// TODO Auto-generated method stub
		
		return assetrepo.updateAsset(asset.getAsset_name(), asset.getAtype().getType_id(), asset.getAsset_number(), asset.getModel_number(), asset.getQuantity() , asset.getAsset_id());
	}

	@Override
	public int updateAssetQuantityByAssetId(Long asid,String qty) {
		// TODO Auto-generated method stub
		return assetrepo.updateAssetQuantityByAssetId(asid, qty);
	}

	@Override
	public int getAssetQuantityByAssetId(Long asid) {
		// TODO Auto-generated method stub
		
		return assetrepo.getQuantiyByAssetId(asid);
	}

}
