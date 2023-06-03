package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
		
		try {
			return emprepo.findAll();
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public Employee getEmployeeById(String empid) {
		// TODO Auto-generated method stub
		if(empid!=null)
		{
			try 
			{		Long eid = Long.valueOf(empid);
					return emprepo.findById(eid).get();
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		
		String new_assets = emp.getMulti_assets();
		
		//System.err.println("New assets are \n"+new_assets);
		AssignedAssets isassigned = null;
		
		int res = emprepo.updateEmployee(emp.getEmp_name(), emp.getEmp_email(), emp.getEmp_contact(), emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id(), emp.getEmp_id());
		
		List<AssignedAssets> assigned_assets = assignassetrepo.getAllAssignedAssetsByEmpId(emp.getEmp_id());
		
		String[] ol_assets = new String[assigned_assets.size()];
		
		String[] nw_assets = new String[new_assets.length()];
		
		nw_assets = new_assets.split(",");
		
		for(int i=0;i<assigned_assets.size();i++)
		{
			ol_assets[i] = assigned_assets.get(i).getAsset().getAsset_id().toString();
		}
		
//		for(int i=0;i<ol_assets.length;i++)
//		{
//			System.err.println("\nOld assets are \n"+ol_assets[i]+"\n");
//		}
//		
//		System.err.println("\n Old and new asset arrays are equal ->>  "+Arrays.equals(ol_assets, nw_assets));
//		
		if(ol_assets.length==nw_assets.length)
		{
			List<String> olist= List.of(ol_assets);
			
			List<String> nlist= List.of(nw_assets);
			
			for(int i=0;i<ol_assets.length;i++)
			{
				if(nlist.contains(ol_assets[i]))
				{
					//System.err.println("\n Asset id "+ol_assets[i]+" from old assets is present in new assets list \n");
					continue;
				}
				else 
				{
					Long asid = Long.valueOf(ol_assets[i]);
					int output = assignassetrepo.deleteAssignedAssetByEmpidAssetId(asid, emp.getEmp_id());
					
					if(output>0)
					{	
						//System.err.println("\n Asset id "+asid+" is retrieved successfully\n");
						int qty = assetrepo.getQuantiyByAssetId(asid);
						qty+=1;
						
						assetrepo.updateAssetQuantityByAssetId(asid, ""+qty);
						
						AssetAssignHistory ahist = new AssetAssignHistory();
						
						Assets ast = new Assets();
						
						Assets getasset = assetrepo.findById(asid).get();
						
						AssetType atype = new AssetType();
						
						atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
						
						ast.setAtype(atype);
						
						ast.setAsset_id(asid);
						ast.setAsset_name(getasset.getAsset_name());
						ast.setAsset_number(getasset.getAsset_number());
						ast.setModel_number(getasset.getModel_number());
						ast.setQuantity(getasset.getQuantity());
						
						ahist.setAsset(ast);
						ahist.setEmployee(emp);
						ahist.setOperation_date(tday);
						ahist.setOperation_time(ttime);
						ahist.setOperation("Asset Retrieved");
						
						assetassignhistrepo.save(ahist);
					}
				}
			}
			
			for(int i=0;i<nw_assets.length;i++)
			{
				if(olist.contains(nw_assets[i]))
				{
					//System.err.println("\n Asset id "+ol_assets[i]+" from old assets is present in new assets list \n");
					continue;
				}
				else
				{
					AssignedAssets assignasset = new AssignedAssets();
					Long asid = Long.valueOf(nw_assets[i]);
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

						qty-=1;
						assetrepo.updateAssetQuantityByAssetId(astid, ""+qty);
						
						AssetAssignHistory ahist = new AssetAssignHistory();
					
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
		
		//If Assets to be assigned are greater than the Already assigned assets
		if(nw_assets.length>ol_assets.length)
		{
			//System.err.println("New assets length is greater than Old ");
			List<String> olist= List.of(ol_assets);
			
			List<String> nlist= List.of(nw_assets);
			
			//System.err.println("\n Old assets list is -->> "+olist.toString()+"\n New Assets list is --->>> "+nlist.toString()+"\n");
			
			for(int i=0;i<nw_assets.length;i++)
			{
				if(olist.contains(nw_assets[i]))
				{
					continue;
				}
				else
				{
					AssignedAssets assignasset = new AssignedAssets();
					Long asid = Long.valueOf(nw_assets[i]);
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

						qty-=1;
						assetrepo.updateAssetQuantityByAssetId(astid, ""+qty);
						
						AssetAssignHistory ahist = new AssetAssignHistory();
					
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
		
		//If Assets to be assigned are smaller than the Already assigned assets
		if(nw_assets.length<ol_assets.length)
		{
			//System.err.println("New assets length is lesser than Old ");
			List<String> olist= List.of(ol_assets);
			
			List<String> nlist= List.of(nw_assets);
			
			//System.err.println("\n Old assets list is -->> "+olist.toString()+"\n New Assets list is --->>> "+nlist.toString()+"\n");
			
			for(int i=0;i<ol_assets.length;i++)
			{
				if(nlist.contains(ol_assets[i]))
				{
					//System.err.println("\n Asset id "+ol_assets[i]+" from new assets is present in old assets list \n");
					continue;
				}
				else
				{
					//System.err.println("\n Asset id "+ol_assets[i]+" from new assets is NOT present in old assets list \n");
					Long asid = Long.valueOf(ol_assets[i]);
					int output = assignassetrepo.deleteAssignedAssetByEmpidAssetId(asid, emp.getEmp_id());
					
					if(output>0)
					{	
						System.err.println("\n Asset id "+asid+" is retrieved successfully\n");
						int qty = assetrepo.getQuantiyByAssetId(asid);
						qty+=1;
						
						assetrepo.updateAssetQuantityByAssetId(asid, ""+qty);
						
						AssetAssignHistory ahist = new AssetAssignHistory();
						
						Assets ast = new Assets();
						
						Assets getasset = assetrepo.findById(asid).get();
						
						AssetType atype = new AssetType();
						
						atype = atyperepo.findById(getasset.getAtype().getType_id()).get();
						
						ast.setAtype(atype);
						
						ast.setAsset_id(asid);
						ast.setAsset_name(getasset.getAsset_name());
						ast.setAsset_number(getasset.getAsset_number());
						ast.setModel_number(getasset.getModel_number());
						ast.setQuantity(getasset.getQuantity());
						
						ahist.setAsset(ast);
						ahist.setEmployee(emp);
						ahist.setOperation_date(tday);
						ahist.setOperation_time(ttime);
						ahist.setOperation("Asset Retrieved");
						
						assetassignhistrepo.save(ahist);
					}
				}
			}

		}
		if(isassigned!=null)
		{
			return 1;
		}
		else {
			return 0;
		}
	}
}
