package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Company;
import com.example.demo.service.CompanyService;

@Controller
public class CompanyController {

	@Autowired
	CompanyService compserv;
	
	@GetMapping("/addcompany")
	public String addCompany()
	{
		return "AddCompany";
	}

	@RequestMapping("/savecompany")
	public String saveCompany(@ModelAttribute("Company") Company company,RedirectAttributes attr)
	{
		Company comp = compserv.saveCompany(company);
		if(comp!=null)
		{
			attr.addFlashAttribute("response", "Company is saved successfully");
			return "redirect:/viewcompanies";
		}
		else {
			attr.addFlashAttribute("reserr", "Company is not saved");
			return "redirect:/viewcompanies";
		}
	}
	
	@GetMapping("/viewcompanies")
	public String viewCompany(Model model)
	{
			List<Company> clist = compserv.getAllCompanies();
		
			model.addAttribute("clist", clist);
			return "ViewCompanies";
		
	}


}
