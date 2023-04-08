package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Company;
import com.example.demo.repository.CompanyRepo;

@Service("compserv")
public class CompanyServImpl implements CompanyService {

	@Autowired
	CompanyRepo comprepo;
	
	@Override
	public Company saveCompany(Company comp) {
		// TODO Auto-generated method stub
		
		if(comp!=null)
		{
			return comprepo.save(comp);
		}
		else {
			return null;
		}
	}

	@Override
	public List<Company> getAllCompanies() {
		// TODO Auto-generated method stub
		return comprepo.findAll();
	}

	@Override
	public Company getCompanyById(String id) {
		// TODO Auto-generated method stub
		Long cid = Long.valueOf(id);
		
		Company comp = comprepo.findById(cid).get();
		
		if(comp!=null)
		{
			return comp;
		}
		else {
			return null;
		}
		
	}

	@Override
	public int updateCompany(Company comp) {
		// TODO Auto-generated method stub
		return 0;
	}

}
