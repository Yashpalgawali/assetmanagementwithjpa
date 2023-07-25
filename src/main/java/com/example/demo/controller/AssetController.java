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

import com.example.demo.models.AssetType;
import com.example.demo.models.Assets;
import com.example.demo.service.AssetService;
import com.example.demo.service.AssetTypeService;

@Controller
public class AssetController {

	@Autowired
	AssetTypeService atypeserv;
	
	
	@GetMapping("/addassettype")
	public String addAssetType()
	{
		return "AddAssetType";
	}
	
	@RequestMapping("/saveassettype")
	public String saveAssetType(@ModelAttribute("AssetType")AssetType atype,RedirectAttributes attr)
	{
		AssetType type = atypeserv.saveAssetType(atype);
		if(type!=null)
		{
			attr.addFlashAttribute("response", "Asset Type is saved successfully");
			return "redirect:/viewassettype";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset Type is not saved ");
			return "redirect:/viewassettype";
		}
	}
	
	@GetMapping("/viewassettype")
	public String viewAssetTypes(Model model)
	{
		List<AssetType> atype = atypeserv.getAllAssetTypes();
		
		model.addAttribute("atype", atype);		
		return "ViewAssetType";
	}
	
	
	@GetMapping("/editassettype/{id}")
	public String editAssetTypeById(@PathVariable("id") String id, Model model ,RedirectAttributes attr)
	{
		AssetType atype = atypeserv.getAssetTypeById(id);
		if(atype!=null)
		{
			model.addAttribute("types", atype);
			return "EditAssetType";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset Type is not found for given ID");
			return "redirect:/viewassettype";
		}
	}
	
	@RequestMapping("/updateassettype")
	public String updateAssetType(@ModelAttribute("AssetType")AssetType atype,RedirectAttributes attr)
	{
		int res = atypeserv.updateAssetType(atype);
		
		if(res>0) {
			attr.addFlashAttribute("response", "Asset Type is Update successfully");
			return "redirect:/viewassettype";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset Type is not updated ");
			return "redirect:/viewassettype";
		}
	}

	
	@Autowired
	AssetService assetserv;
	
	@GetMapping("/addasset")
	public String addAssets(Model model)
	{
		List<AssetType> alist = atypeserv.getAllAssetTypes();
		model.addAttribute("alist", alist);
		return "AddAsset";
	}
	
	@RequestMapping("/saveasset")
	public String saveAssets(@ModelAttribute("Assets")Assets asset,RedirectAttributes attr)
	{
		
		Assets ast = assetserv.saveAssets(asset);
		if(ast!=null)
		{
			attr.addFlashAttribute("response", "Asset Type is saved successfully");
			return "redirect:/viewassets";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset Type is not saved ");
			return "redirect:/viewassets";
		}
	}
	
	@GetMapping("/viewassets")
	public String viewAssets(Model model)
	{
		List<Assets> asset = assetserv.getAllAssets();
		
		model.addAttribute("aslist", asset);		
		return "ViewAssets";
	}
	
	
	@GetMapping("/editasset/{id}")
	public String editAssetByIs(@PathVariable("id") String id,Model model, RedirectAttributes attr)
	{
		Assets asset = assetserv.getAssetsById(id);
		if(asset!=null){
			model.addAttribute("atype", atypeserv.getAllAssetTypes());
			model.addAttribute("asset", asset);
			return "EditAsset";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset Type is not found for given Id");
			return "redirect:/viewassets";
		}
	}
	
	@RequestMapping("/updateasset")
	public String updateAsset(@ModelAttribute("Assets")Assets ast,RedirectAttributes attr)
	{
		int res = assetserv.updateAssets(ast);
		if(res > 0){
			attr.addFlashAttribute("response", "Asset  is updated successfully");
			return "redirect:/viewassets";
		}
		else {
			attr.addFlashAttribute("reserr", "Asset is not updated");
			return "redirect:/viewassets";
		}
	}
}
