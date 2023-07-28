package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Company;
import com.example.demo.models.Department;
import com.example.demo.service.CompanyService;
import com.example.demo.service.DepartmentService;

@Controller
public class DepartmentController {

	@Autowired
	DepartmentService deptserv;
	
	@Autowired
	CompanyService compserv;
	
	@GetMapping("/adddepartment")
	public String addDepartment(Model model)
	{
		List<Company> clist = compserv.getAllCompanies();
		
		model.addAttribute("clist", clist);
		return "AddDepartment";
	}
	
	
	@RequestMapping("/savedepartment")
	public String saveDepartment(@ModelAttribute("Department") Department dept,RedirectAttributes attr)
	{
		Department depart = deptserv.saveDepartment(dept);
		
		if(depart!=null)
		{
			attr.addFlashAttribute("response", "Department is saved Successfully");
			return "redirect:/viewdepartments";
		}	
		else {
			attr.addFlashAttribute("reserr", "Department is not saved ");
			return "redirect:/viewdepartments";
		}
	}
	
	@GetMapping("/editdeptbyid/{id}")
	public String getDepartmentByDeptId(@PathVariable("id")String id,Model model,RedirectAttributes attr)
	{
		Department dept = deptserv.getDepartmentById(id);
		if(dept!=null)
		{
			model.addAttribute("clist", compserv.getAllCompanies());
			model.addAttribute("dept", dept);
			return "EditDepartment";
		}
		else
		{
			attr.addFlashAttribute("reserr", "No Department Found");
			return "redirect:/viewdepartments";
		}
	}
	
	
	@RequestMapping("/updatedepartment")
	public String updateDepartment(@ModelAttribute("Department")Department dept,RedirectAttributes attr)
	{
		int result = deptserv.updateDepartment(dept);
		if(result > 0 )
		{
			attr.addFlashAttribute("response", "Department is updated Successfully");
			return "redirect:/viewdepartments";
		}
		else
		{
			attr.addFlashAttribute("reserr", "Department is not Updated ");
			return "redirect:/viewdepartments";
		}
		
	}
	
	@GetMapping("/viewdepartments")
	public String viewAllDepartments(Model model){
		model.addAttribute("dlist", deptserv.getAllDepartments());
		return "ViewDepartments";
	}
	
	
	@RequestMapping("/getdeptbycompid/{id}")@ResponseBody
	public List<Department> getDepartmentByCompanyId(@PathVariable("id")String id){
		return deptserv.getDepartmentByCompanyId(id);
	}
	
}
