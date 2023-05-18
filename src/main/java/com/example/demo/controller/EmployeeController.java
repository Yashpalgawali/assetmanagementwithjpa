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
	
	@RequestMapping("/saveemployee")@ResponseBody
	public String saveEmployee(@ModelAttribute("Employee")Employee empl,RedirectAttributes attr)
	{
		String asset_ids = empl.getMulti_assets();
		
	
		Employee emp = empserv.saveEmployee(empl);
		if(emp!=null)
		{
			AssignedAssets assignasset = new AssignedAssets();
			char[] chararr = asset_ids.toCharArray();
			
			for(int i=0;i<chararr.length;i++)
			{
				if(Character.isDigit(chararr[i]))
				{
					long asid = Character.getNumericValue(chararr[i]);
					
//					Assets ast = new Assets();
//					
//					String astid =String.valueOf(asid);
//					Assets get_assets = assetserv.getAssetsById(astid);
//					
//					ast.setAsset_id(asid);
//					ast.setAsset_name(get_assets.getAsset_name());
//					ast.setAtype(atypeserv.getAssetTypeById(""+get_assets.getAtype().getType_id()));
//					ast.setAsset_number(get_assets.getAsset_number());
//					ast.setModel_number(get_assets.getModel_number());
//					ast.setQuantity(get_assets.getQuantity());
					
					assignasset.setEmployee(emp);
					assignasset.setAssign_date(ddate.format(today.now()));
					assignasset.setAssign_time(dtime.format(today.now()));
					assignasset.setEmp_id(emp.getEmp_id());
					//assignasset.setAsset(ast);
					
					assignasset = assignserv.saveAssignedAssets(assignasset);
					if(assignasset!=null)
					{
						System.err.println("Asset with Id "+asid+" saved\n");
						int qty = assetserv.getAssetQuantityByAssetId(asid);
						qty-=1;
						
						assetserv.updateAssetQuantityByAssetId(asid, ""+qty);
					}
					
					AssetAssignHistory ahist = new AssetAssignHistory();
					
					ahist.setAsset_id(asid);
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
	
	@GetMapping("/editempassignassetbyempid/{id}")@ResponseBody
	public String editEmployeeByEmpId(@PathVariable("id") String empid,Model model,RedirectAttributes attr)
	{
		Employee emp = empserv.getEmployeeById(empid);
		
		Long eid = Long.valueOf(empid);
		
		List<AssignedAssets> aslist =  assignserv.getAssignedAssetsByEmpId(eid);
		
		System.err.println("\n"+aslist.toString()+"\n");
		
		return ""+emp.toString();
	}
	
}
