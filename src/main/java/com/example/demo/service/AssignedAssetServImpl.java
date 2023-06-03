package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AssignedAssets;
import com.example.demo.repository.AssetRepo;
import com.example.demo.repository.AssignedAssetsRepo;

@Service("assignassetserv")
public class AssignedAssetServImpl implements AssignedAssetService {

	@Autowired
	AssignedAssetsRepo assignassetrepo;
	
	@Autowired
	AssetRepo assetrepo;
	

	@Override
	public AssignedAssets saveAssignedAssets(AssignedAssets assign) {
		// TODO Auto-generated method stub
		
		return assignassetrepo.save(assign);
	}

	@Override
	public List<AssignedAssets> getAllAssignedAssets() {
		// TODO Auto-generated method stub
		
		List<AssignedAssets> alist =  assignassetrepo.getAllAssignedAssets(); 
		
		System.err.println("\nInside getAllAssignedAssets service method\n");
		alist.stream().forEach(e->System.err.println(e));		
		return alist;
	}

	@Override
	public List<AssignedAssets> getAssignedAssetsByEmpId(Long empid) {
		// TODO Auto-generated method stub
		return assignassetrepo.getAllAssignedAssetsByEmpId(empid);
	}

	@Override
	public int retrieveAssetByEmpId(AssignedAssets assign) {
		// TODO Auto-generated method stub
		String asset_ids = assign.getMulti_assets();
		char[] chararr =  asset_ids.toCharArray();
		
		int res = 0;
		
		for(int i=0;i<chararr.length;i++)
		{
			if(Character.isDigit(chararr[i]))
			{
				Long asid = (long)Character.getNumericValue(chararr[i]);
				
				res  = assignassetrepo.deleteAssignedAssetByEmpidAssetId(asid, assign.getEmp_id());
				 
				int asset_qty = assetrepo.getQuantiyByAssetId(asid);
				asset_qty +=1;
				
				int asres = assetrepo.updateAssetQuantityByAssetId(asid, ""+asset_qty);
				
			}
		}
		return res;
	}

	@Override
	public List<Object[]> getAllAssignedassetsGroup() {
		// TODO Auto-generated method stub
		try {
			return assignassetrepo.getAllNewAssignedAssets();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
