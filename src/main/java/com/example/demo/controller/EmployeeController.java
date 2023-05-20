package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.AssetAssignHistory;
import com.example.demo.models.AssetType;
import com.example.demo.models.Assets;
import com.example.demo.models.AssignedAssets;
import com.example.demo.models.Employee;
import com.example.demo.service.AssetAssignHistService;
import com.example.demo.service.AssetService;
import com.example.demo.service.AssetTypeService;
import com.example.demo.service.AssignedAssetService;
import com.example.demo.service.CompanyService;
import com.example.demo.service.DesignationService;
import com.example.demo.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeService empserv;
	
	@Autowired
	CompanyService compserv;
	
	@Autowired
	DesignationService desigserv;
	
	@Autowired
	AssetService assetserv;
	
	@Autowired
	AssignedAssetService assignserv;
	
	@Autowired
	AssetAssignHistService ahistserv;
	
	@Autowired
	AssetTypeService atypeserv;
	
	private LocalDateTime today;
	
	private DateTimeFormatter ddate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter dtime = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private String tday=ddate.format(today.now()),ttime=dtime.format(today.now());
	
	@GetMapping("/addemployee")
	public String addEmployee(Model model)
	{
		model.addAttribute("clist", compserv.getAllCompanies());
		model.addAttribute("desiglist", desigserv.getAllDesignations());
		model.addAttribute("aslist", assetserv.getAllAssets());
		return "AddEmployee";
	}
	
	@RequestMapping("/saveemployee")
	public String saveEmployee(@ModelAttribute("Employee")Employee empl,RedirectAttributes attr)
	{
		String asset_ids = empl.getMulti_assets();
		
		AssignedAssets isassigned = null;
		Employee emp = empserv.saveEmployee(empl);
		if(emp!=null)
		{
			String[] asset_arr = asset_ids.split(",");
			
			for(int i=0;i<asset_arr.length;i++)
			{
				AssignedAssets assignasset = new AssignedAssets();
				int qty =0;
				
				Long astid = Long.valueOf(asset_arr[i]);
				
				Assets ast = new Assets();
				Assets getasset = assetserv.getAssetsById(""+astid);
				
				AssetType atype = new AssetType();
				
				atype = atypeserv.getAssetTypeById(""+getasset.getAtype().getType_id());
				
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
				
				isassigned = assignserv.saveAssignedAssets(assignasset);
				
				if(isassigned!=null)
				{	System.err.println(getasset.getAsset_name()+ " is assigned succcessfully");
					qty = (Integer)assetserv.getAssetQuantityByAssetId(astid);
					qty-=1;
					assetserv.updateAssetQuantityByAssetId(astid, ""+qty);
					System.err.println("Asset Quantity is reduced of Id "+astid+" is reduced");
					
					AssetAssignHistory ahist = new AssetAssignHistory();
				
					ahist.setAsset(ast);
					ahist.setEmployee(emp);
					ahist.setOperation_date(tday);
					ahist.setOperation_time(ttime);
					ahist.setOperation("Asset Assigned");
					
					ahistserv.saveAssetAssignHistory(ahist);
				}
			}

			attr.addFlashAttribute("response", "Assets are assigned successfully");
			return "redirect:/viewassignedassets";
		}
		else {
			attr.addFlashAttribute("reserr", "Employee is not Saved");
			return "redirect:/viewassignedassets";
		} 
	}
	
	
	@GetMapping("/viewassignedassets")
	public String viewAssignedAssets(Model model)
	{
		List<AssignedAssets> aslist = assignserv.getAllAssignedAssets();
		model.addAttribute("aslist", aslist);
		return "ViewAssignedAssets";
	}
	
	@GetMapping("/viewallemployees")
	public String viewAllEmployees(Model model)
	{
		List<Employee> elist = empserv.getAllEmployees();
		
		model.addAttribute("elist", elist);
		return "ViewAllEmployees";
	}
	
	@GetMapping("/retrieveassetsbyempid/{id}")
	public String retrieveAssets(@PathVariable("id") Long id,Model model,RedirectAttributes attr)
	{
		List<AssignedAssets> assign = assignserv.getAssignedAssetsByEmpId(id);
		Employee empl = null;
		if(assign.size()>0)
		{	
			Long[] strArray = new Long[assign.size()];
			for(int i=0;i<assign.size();i++)
			{
				empl = assign.get(i).getEmployee();
				strArray[i] = assign.get(i).getAsset().getAsset_id();
			}
			
			model.addAttribute("aslist", 	assetserv.getAllAssets());
			model.addAttribute("emp", 		empl);
			model.addAttribute("assignedlist", strArray);
			return "RetrieveAssets";
		}
		else
		{
			attr.addFlashAttribute("reserr", "No Assets are assigned ");
			return "redirect:/viewallemployees";
		}
	}
	
	@PostMapping("/updateretrieveassets")
	public String updateRetrieveAssets(@ModelAttribute("AssignedAssets")AssignedAssets assign,RedirectAttributes attr)
	{
		int val = assignserv.retrieveAssetByEmpId(assign);
		if(val>0) {
			attr.addFlashAttribute("response", "Assets are assigned successfully");
			return "redirect:/viewassignedassets";
		}
		else
		{
			attr.addFlashAttribute("reserr", "Assets are not assigned successfully");
			return "redirect:/viewassignedassets";
		}
	}
	 
	@GetMapping("/viewemphistbyempid/{id}")
	public String viewEmployeeHistoryByEmpId(@PathVariable("id") String id,Model model, RedirectAttributes attr)
	{
		List<AssetAssignHistory> ahist = ahistserv.getAssetAssignHistoryByEmpId(id);
		
		
		model.addAttribute("ahist", ahist);
		model.addAttribute("emp", empserv.getEmployeeById(id));
		return "ViewEmployeeHistory";
	}
	
	@GetMapping("/editempassignassetbyempid/{id}")
	public String editEmployeeByEmpId(@PathVariable("id") String empid,Model model,RedirectAttributes attr)
	{
		Employee emp = empserv.getEmployeeById(empid);
		List<AssignedAssets> aslist =  assignserv.getAssignedAssetsByEmpId(Long.valueOf(empid));
		
		String assigned_assets = "",assigned_asset_type="",assigned_ids="";
		
		Long[] strArray = new Long[aslist.size()];
		
		for(int i=0;i<aslist.size();i++)
		{
			if(i==0){
				assigned_assets = assigned_assets+aslist.get(i).getAsset().getAsset_name()+"("+aslist.get(i).getAsset().getModel_number()+")";
				assigned_asset_type =assigned_asset_type+aslist.get(i).getAsset().getAtype().getType_name();
				//assigned_ids = assigned_ids+aslist.get(i).getAsset().getAsset_id();
				strArray[i] = aslist.get(i).getAsset().getAsset_id();
			}
			else{
				assigned_assets = assigned_assets+","+aslist.get(i).getAsset().getAsset_name()+"("+aslist.get(i).getAsset().getModel_number()+")";
				assigned_asset_type =assigned_asset_type+","+aslist.get(i).getAsset().getAtype().getType_name();
				//assigned_ids = assigned_ids+","+aslist.get(i).getAsset().getAsset_id();
			
				strArray[i] =aslist.get(i).getAsset().getAsset_id(); 
				//Long.valueOf(assigned_ids);
			}
		}
		System.err.println("Employee -->> "+emp.toString());
		
		model.addAttribute("clist", compserv.getAllCompanies());
		model.addAttribute("desiglist", desigserv.getAllDesignations());
		model.addAttribute("aslist", assetserv.getAllAssets());
		model.addAttribute("emp", emp);
		model.addAttribute("assignasset", assigned_assets);
		model.addAttribute("assignedlist", strArray);
		model.addAttribute("atlist",  atypeserv.getAllAssetTypes());
		model.addAttribute("atypes", assigned_asset_type);
		return "EditEmployee";
	}
	
}
