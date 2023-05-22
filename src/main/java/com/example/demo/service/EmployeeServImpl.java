package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AssetAssignHistory;
import com.example.demo.models.AssetType;
import com.example.demo.models.Assets;
import com.example.demo.models.AssignedAssets;
import com.example.demo.models.Employee;
import com.example.demo.repository.AssetAssignHistoryRepo;
import com.example.demo.repository.AssetRepo;
import com.example.demo.repository.AssetTypeRepo;
import com.example.demo.repository.AssignedAssetsRepo;
import com.example.demo.repository.EmployeeRepo;

@Service("empserv")
public class EmployeeServImpl implements EmployeeService {

	@Autowired
	EmployeeRepo emprepo;
	
	@Autowired
	AssignedAssetsRepo assignassetrepo;
	
	@Autowired
	AssetRepo assetrepo;
	
	@Autowired
	AssetAssignHistoryRepo assetassignhistrepo; 
	
	@Autowired
	AssetTypeRepo atyperepo;
	
	private LocalDateTime today;
	
	private DateTimeFormatter ddate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter dtime = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private String tday=ddate.format(today.now()),ttime=dtime.format(today.now());
	
	@Override
	public Employee saveEmployee(Employee emp) {
		// TODO Auto-generated method stub
	
		if(emp!=null)
		{
			return emprepo.save(emp);
		}
		else {
			return null;
		}
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return emprepo.findAll();
	}

	@Override
	public Employee getEmployeeById(String empid) {
		// TODO Auto-generated method stub
		if(empid!=null)
		{
			Long eid = Long.valueOf(empid);
			return emprepo.findById(eid).get();
		}
		else {
			return null;
		}
	}

	@Override
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		
		String new_assets = emp.getMulti_assets();
		
		String old_assets = "";
					
		int res = emprepo.updateEmployee(emp.getEmp_name(), emp.getEmp_email(), emp.getEmp_contact(), emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id(), emp.getEmp_id());
		
		List<AssignedAssets> assigned_assets = assignassetrepo.getAllAssignedAssetsByEmpId(emp.getEmp_id());
		
		for(int i=0;i<assigned_assets.size();i++)
		{
			if(i==0)
				old_assets = assigned_assets.get(i).getAsset().getAsset_id().toString();
			else
				old_assets = old_assets+","+assigned_assets.get(i).getAsset().getAsset_id().toString();
		}
		
		AssignedAssets isassigned = null;
		if(old_assets.length()==new_assets.length())
		{
			if(!old_assets.equals(new_assets))
			{
				char[] oldarr = old_assets.toCharArray();
				for(int i=0;i<old_assets.length();i++)
				{
					if(Character.isDigit(oldarr[i]))
					{
						int asid = Character.getNumericValue(oldarr[i]);
						
						if(!new_assets.contains(""+asid))
						{
							int output = assignassetrepo.deleteAssignedAssetByEmpidAssetId((long)asid, emp.getEmp_id());
							if(output>0)
							{
								int qty = assetrepo.getQuantiyByAssetId((long)asid);
								qty+=1;
								
								assetrepo.updateAssetQuantityByAssetId((long)asid, ""+qty);
								
								AssetAssignHistory ahist = new AssetAssignHistory();
								
								Assets ast = new Assets();
								
								Assets getasset = assetrepo.findById((long)asid).get();
								
								AssetType atype = new AssetType();
								
								atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
								
								ast.setAtype(atype);
								
								ast.setAsset_id((long)asid);
								ast.setAsset_name(getasset.getAsset_name());
								ast.setAsset_number(getasset.getAsset_number());
								ast.setModel_number(getasset.getModel_number());
								ast.setQuantity(getasset.getQuantity());
								
								ahist.setAsset(ast);
								ahist.setEmployee(emp);
								ahist.setOperation_date(tday);
								ahist.setOperation_time(ttime);
								ahist.setOperation("Asset Assigned");
								
								assetassignhistrepo.save(ahist);
							}
						}
					}
				}
				//System.err.println("Both assets are equal \nAlreaddy assigned assets are "+old_assets+"\n New Assets to be assigned are -->>>> "+new_assets+"\n");
				char[] chararr = new_assets.toCharArray();
				for(int i=0;i<new_assets.length();i++)
				{
					System.err.println("New asset id is ->> "+new_assets+"\n if already assigned and new assigned values are "+old_assets.contains(new_assets));
					
					for(int j=0;j<chararr.length ;j++)
					{
						if(Character.isDigit(chararr[j]))
						{
							int asid = Character.getNumericValue(chararr[j]);
							
							if(old_assets.contains(""+asid))
							{
								continue;
							}
							else
							{
								AssignedAssets assignasset = new AssignedAssets();
								int qty =0;
								
								Long astid = Long.valueOf(asid);
								
								Assets ast = new Assets();
								
								assetrepo.findById(astid);
								Assets getasset = assetrepo.findById(astid).get();
								
								AssetType atype = new AssetType();
								
								atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
								
								ast.setAtype(atype);
								
								ast.setAsset_id(astid);
								ast.setAsset_name(getasset.getAsset_name());
								ast.setAsset_number(getasset.getAsset_number());
								ast.setModel_number(getasset.getModel_number());
								ast.setQuantity(getasset.getQuantity());
								
								assignasset.setEmployee(emp);
								assignasset.setAsset(ast);
							
								assignasset.setAssign_date(ddate.format(today.now()));
								assignasset.setAssign_time(dtime.format(today.now()));
								
								isassigned = assignassetrepo.save(assignasset);
								
								if(isassigned!=null)
								{	
									qty = assetrepo.getQuantiyByAssetId(astid);
//									qty = (Integer)assetserv.getAssetQuantityByAssetId(astid);
									qty-=1;
									assetrepo.updateAssetQuantityByAssetId(astid, ""+qty);
									
									AssetAssignHistory ahist = new AssetAssignHistory();
								
									ahist.setAsset(ast);
									ahist.setEmployee(emp);
									ahist.setOperation_date(tday);
									ahist.setOperation_time(ttime);
									ahist.setOperation("Asset Assigned");
									
									assetassignhistrepo.save(ahist);
									//ahistserv.saveAssetAssignHistory(ahist);
								}
							}
						}
					}	
				}
			}
			
			return res;
		}
		else
		{
			if(old_assets.length()>new_assets.length())
			{
				System.err.println("Already assets are greater than new assets \nAlready assets length = "+old_assets.length()+"\nNew Assets Length is = "+new_assets.length());
			}
			if(old_assets.length()<new_assets.length())
			{
				System.err.println("Already assets are lesser than new assets   \nAlready assets length = "+old_assets.length()+"\nNew Assets Length is = "+new_assets.length());
			}
			return res;
		}
		
		
	}
}
