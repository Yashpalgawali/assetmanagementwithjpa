package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.AssetAssignHistory;
import com.example.demo.models.Assets;
import com.example.demo.models.AssignedAssets;
import com.example.demo.models.Employee;
import com.example.demo.service.AssetAssignHistService;
import com.example.demo.service.AssetService;
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
		
		int resassign=0;
		
		Employee emp = empserv.saveEmployee(empl);
		if(emp!=null)
		{
			AssignedAssets assignasset = new AssignedAssets();
			char[] chararr = asset_ids.toCharArray();
			
			for(int i=0;i<chararr.length;i++)
			{
				if(Character.isDigit(chararr[i]))
				{
					int asid = Character.getNumericValue(chararr[i]);
					
					Assets ast = new Assets();
					ast.setAsset_id((long)asid);
					
					assignasset.setEmployee(emp);
					assignasset.setAssign_date(tday);
					assignasset.setAssign_time(ttime);
					assignasset.setAsset(ast);
					
					assignasset = assignserv.saveAssignedAssets(assignasset);
					if(assignasset!=null)
					{
						System.err.println("assets assigned successfully ");
						int qty = assetserv.getAssetQuantityByAssetId((long)asid);
						
						System.err.println("Total quantity of asset id -> "+asid+" is ->>> "+qty);
						qty-=1;
						
						assetserv.updateAssetQuantityByAssetId((long)asid, ""+qty);
					}
					
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
			return "redirect:/viewassignassets";
		}
		else {
			attr.addFlashAttribute("reserr", "Employee is not Saved");
			return "redirect:/viewassignassets";
		}
		
	}
}
