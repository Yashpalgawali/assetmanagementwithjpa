package com.example.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.exporttoexcel.ExportAssetAssignHistory;
import com.example.demo.exporttoexcel.ExportAssignedAssets;
import com.example.demo.models.AssetAssignHistory;
import com.example.demo.models.AssetType;
import com.example.demo.models.Assets;
import com.example.demo.models.AssignedAssets;
import com.example.demo.models.Company;
import com.example.demo.models.Department;
import com.example.demo.models.Designation;
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
				{	
					qty = (Integer)assetserv.getAssetQuantityByAssetId(astid);
					qty-=1;
					assetserv.updateAssetQuantityByAssetId(astid, ""+qty);
					
					AssetAssignHistory ahist = new AssetAssignHistory();
				
					ahist.setAsset(ast);
					ahist.setEmployee(emp);
					ahist.setOperation_date(ddate.format(today.now()));
					ahist.setOperation_time(dtime.format(today.now()));
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
	
	
	//This method returns the assigned assets in single column 
	@GetMapping("/viewassignedassets")
	public String viewAllAssignedAssets(Model model)
	{
		List<AssignedAssets> alist = new ArrayList<AssignedAssets>();
		List<Object[]>  aslist = assignserv.getAllAssignedassetsGroup();
		
		if(aslist.size() >0)
		{
			aslist.forEach(ast->{
					AssignedAssets asts = new AssignedAssets();
					
					String assets= "",asset_type="";
					
					asts.setAssigned_asset_id(Long.valueOf(ast[0].toString()));
					asts.setAssign_date(ast[1].toString());
					asts.setAssign_time(ast[2].toString());
					asts.setAsset_id(Long.valueOf(ast[3].toString()));
					asts.setEmp_id((Long.valueOf(ast[4].toString())));
					
					assets = Stream.of(ast[5].toString().split(",")).collect(Collectors.toList()).toString();
					
					assets = assets.replace("[", "");
					assets = assets.replace("]", "");
					
					asts.setAssigned(assets);
					
					asset_type = Stream.of(ast[6].toString().split(",")).collect(Collectors.toList()).toString();
					asset_type = asset_type.replace("[", "");
					asset_type = asset_type.replace("]", "");
					
					asts.setAssigned_types(asset_type);
					
					Employee emp = new Employee();
					
					emp.setEmp_name(ast[7].toString());
					emp.setEmp_email(ast[8].toString());
					emp.setEmp_contact(ast[9].toString());
					
					Designation desig = new Designation();
					desig.setDesig_id((Long.valueOf(ast[10].toString())));
					desig.setDesig_name(ast[11].toString());

					Department dept = new Department();
					dept.setDept_id((Long.valueOf(ast[12].toString())));
					dept.setDept_name(ast[13].toString());
					
					Company comp = new Company();
					comp.setComp_id((Long.valueOf(ast[14].toString())));
					comp.setComp_name(ast[15].toString());
					
					String mod_num = "";
					
					mod_num = Stream.of(ast[16].toString().split(",")).collect(Collectors.toList()).toString();
					mod_num = mod_num.replace("[", "");
					mod_num = mod_num.replace("]", "");
					
					asts.setModel_numbers(mod_num);
					dept.setCompany(comp);
					
					emp.setDepartment(dept);
					emp.setDesignation(desig);
					asts.setEmployee(emp);
				
					alist.add(asts);
			});
		
			model.addAttribute("aslist", alist);
			return "ViewAssignedAssets";
		}
		else {
				return "redirect:/viewallemployees";
		}
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
		else{
			attr.addFlashAttribute("reserr", "Assets are not assigned successfully");
			return "redirect:/viewassignedassets";
		}
	}
	 
	@GetMapping("/viewemphistbyempid/{id}")
	public String viewEmployeeHistoryByEmpId(@PathVariable("id") String id,Model model, RedirectAttributes attr)
	{
		List<AssetAssignHistory> ahist = ahistserv.getAssetAssignHistoryByEmpId(id);
		
		if(ahist.size()>0)
		{	
			model.addAttribute("ahist", ahist);
			model.addAttribute("emp", empserv.getEmployeeById(id));
			return "ViewEmployeeHistory";
		}
		else {
			attr.addFlashAttribute("reserr", "No History found");
			return "redirect:/viewassignedassets";
		}
	}
	
	@GetMapping("/editempassignassetbyempid/{id}")
	public String editEmployeeByEmpId(@PathVariable("id") String empid,Model model,RedirectAttributes attr)
	{
		Employee emp = empserv.getEmployeeById(empid);
		if(emp!=null) {
			List<AssignedAssets> aslist =  assignserv.getAssignedAssetsByEmpId(Long.valueOf(empid));
			
			String assigned_assets = "",assigned_asset_type="";
			
			Long[] strArray = new Long[aslist.size()];
			
			for(int i=0;i<aslist.size();i++)
			{
				if(i==0){
					assigned_assets = assigned_assets+aslist.get(i).getAsset().getAsset_name()+"("+aslist.get(i).getAsset().getModel_number()+")";
					assigned_asset_type =assigned_asset_type+aslist.get(i).getAsset().getAtype().getType_name();
					strArray[i] = aslist.get(i).getAsset().getAsset_id();
				}
				else{
					assigned_assets = assigned_assets+","+aslist.get(i).getAsset().getAsset_name()+"("+aslist.get(i).getAsset().getModel_number()+")";
					assigned_asset_type =assigned_asset_type+","+aslist.get(i).getAsset().getAtype().getType_name();
					strArray[i] =aslist.get(i).getAsset().getAsset_id(); 
				}
			}
			
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
		else {
			attr.addFlashAttribute("reserr","No Employee found for given ID");
			return "redirect:/viewallemployees";
		}

	}
	
	@RequestMapping("/updateassignasset")
	public String updateAssignedAssets(@ModelAttribute("Employee")Employee emp,RedirectAttributes attr)
	{
		int res = empserv.updateEmployee(emp);
		if(res>0){
			attr.addFlashAttribute("response", "Assets are assigned successfully");
			return "redirect:/viewassignedassets";
		}
		else{
			attr.addFlashAttribute("reserr", "Assets are not assigned ");
			return "redirect:/viewassignedassets";
		}
	}
	
		@RequestMapping("/exportassignedassets/excel")
	    public void exportToExcel(HttpServletResponse response) throws IOException {
	        response.setContentType("application/octet-stream");
	        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String currentDateTime = dateFormatter.format(new Date());
	         
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=Assigned_Assets_" + currentDateTime + ".xls";
	        response.setHeader(headerKey, headerValue);
	         
	        List<AssignedAssets> alist = new ArrayList<AssignedAssets>();
			List<Object[]>  aslist = assignserv.getAllAssignedassetsGroup();
			
			if(aslist.size() >0)
			{
				aslist.forEach(ast->{
						AssignedAssets asts = new AssignedAssets();
						
						String assets= "",asset_type="";
						
						asts.setAssigned_asset_id(Long.valueOf(ast[0].toString()));
						asts.setAssign_date(ast[1].toString());
						asts.setAssign_time(ast[2].toString());
						asts.setAsset_id(Long.valueOf(ast[3].toString()));
						asts.setEmp_id((Long.valueOf(ast[4].toString())));
						
						assets = Stream.of(ast[5].toString().split(",")).collect(Collectors.toList()).toString();
						
						assets = assets.replace("[", "");
						assets = assets.replace("]", "");
						
						asts.setAssigned(assets);
						
						asset_type = Stream.of(ast[6].toString().split(",")).collect(Collectors.toList()).toString();
						asset_type = asset_type.replace("[", "");
						asset_type = asset_type.replace("]", "");
						
						asts.setAssigned_types(asset_type);
						
						Employee emp = new Employee();
						
						emp.setEmp_name(ast[7].toString());
						emp.setEmp_email(ast[8].toString());
						emp.setEmp_contact(ast[9].toString());
						
						Designation desig = new Designation();
						desig.setDesig_id((Long.valueOf(ast[10].toString())));
						desig.setDesig_name(ast[11].toString());

						Department dept = new Department();
						dept.setDept_id((Long.valueOf(ast[12].toString())));
						dept.setDept_name(ast[13].toString());
						
						Company comp = new Company();
						comp.setComp_id((Long.valueOf(ast[14].toString())));
						comp.setComp_name(ast[15].toString());
						
						String mod_num = "";
						
						mod_num = Stream.of(ast[16].toString().split(",")).collect(Collectors.toList()).toString().replace("[", "").replace("]", "");
						mod_num = mod_num.replace("[", "");
						mod_num = mod_num.replace("]", "");
						
						asts.setModel_numbers(mod_num);
						dept.setCompany(comp);
						
						emp.setDepartment(dept);
						emp.setDesignation(desig);
						asts.setEmployee(emp);
					
						alist.add(asts);
				});
			}
	        ExportAssignedAssets excelExporter = new ExportAssignedAssets(alist);
	         
	        excelExporter.export(response);    
	    } 
		
		
		
		@RequestMapping("/exportassignshistory/excel/{id}")
	    public void exportToExcel(HttpServletResponse response,@PathVariable("id")Long empid ) throws IOException {
	        response.setContentType("application/octet-stream");
	        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String currentDateTime = dateFormatter.format(new Date());
	         
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=Assigned_Assets_History" + currentDateTime + ".xls";
	        response.setHeader(headerKey, headerValue);
	         
	        List<AssetAssignHistory> alist = ahistserv.getAssetAssignHistoryByEmpId(""+empid);
	        
	        ExportAssetAssignHistory ahist = new ExportAssetAssignHistory(alist);
	        ahist.export(response);
		}
}
