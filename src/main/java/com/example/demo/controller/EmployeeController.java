package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Employee;
import com.example.demo.service.AssetService;
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
		return ""+empl.getEmp_name()+" &nbsp; IDs ->> "+empl.getMulti_assets();
	}
}
