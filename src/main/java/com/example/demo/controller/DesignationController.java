package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Designation;
import com.example.demo.service.DesignationService;

@Controller
public class DesignationController {

	@Autowired
	DesignationService desigserv;
	
	@GetMapping("/adddesignation")
	public String addDesignation()
	{
		return "AddDesignation";
	}
	
	@RequestMapping("/savedesignation")
	public String saveDesignation(@ModelAttribute("Designation")Designation desig,RedirectAttributes attr)
	{
		Designation designation = desigserv.saveDesignation(desig);
		
		if(designation !=null)
		{
			attr.addFlashAttribute("response", "Designation is Saved Successfully");
			return "redirect:/viewdesignation";
		}
		else {
			attr.addFlashAttribute("reserr", "Designation is not Saved ");
			return "redirect:/viewdesignation";
		}
	}
	
	@GetMapping("/viewdesignation")
	public String viewDesignations(Model model)
	{
		List<Designation> dlist = desigserv.getAllDesignations();
		
		model.addAttribute("dlist", dlist);
		return "ViewDesignation";
	}
	
	@GetMapping("/editdesignation/{id}")
	public String editDesignationById(@PathVariable("id")String id,Model model,RedirectAttributes attr)
	{
		Designation desig = desigserv.getDesignationById(id);
		if(desig!=null)
		{
			model.addAttribute("desig", desig);
			return "EditDesignation";
		}
		else {
			attr.addFlashAttribute("reserr", "No designation found for given Id");
			return "redirect:/viewdesignation";
		}
	}
	
	@RequestMapping("/updatedesignation")
	public String updateDesignation(@ModelAttribute("Designation") Designation desig,RedirectAttributes attr)
	{
		int res= desigserv.updateDesignation(desig);
		if(res>0)
		{
			attr.addFlashAttribute("response", "Designation is Updated Successfully");
			return "redirect:/viewdesignation";
		}
		else {
		
			attr.addFlashAttribute("reserr", "Designation is not Updated");
			return "redirect:/viewdesignation";
		}
	
	}
}
