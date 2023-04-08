package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Designation;
import com.example.demo.repository.DesignationRepo;

@Service("desigserv")
public class DesignationServiceImpl implements DesignationService {

	@Autowired
	DesignationRepo desigrepo;
	
	@Override
	public Designation saveDesignation(Designation desig) {
		// TODO Auto-generated method stub
		return desigrepo.save(desig);
	}

	@Override
	public List<Designation> getAllDesignations() {
		// TODO Auto-generated method stub
		return desigrepo.findAll();
	}

	@Override
	public Designation getDesignationById(String desigid) {
		// TODO Auto-generated method stub
		Long did = Long.valueOf(desigid);
		
		Designation desig = desigrepo.findById(did).get();
		if(desig!=null)
		{
			return desig;
		}
		else {
			return null;
		}
		
	}

	@Override
	public int updateDesignation(Designation desig) {
		// TODO Auto-generated method stub
		return desigrepo.updateDesignation(desig.getDesig_id(), desig.getDesig_name());
	}

}
