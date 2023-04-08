package com.example.demo.service;

import java.util.List;

import com.example.demo.models.Designation;

public interface DesignationService {

public Designation saveDesignation(Designation dept);
	
	public List<Designation> getAllDesignations();
	
	public Designation getDesignationById(String deptid);
	
	public int updateDesignation(Designation dept);
	
}
