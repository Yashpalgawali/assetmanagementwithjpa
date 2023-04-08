package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Department;
import com.example.demo.repository.DepartmentRepo;

@Service("deptserv")
public class DepartmentServImpl implements DepartmentService {

	@Autowired
	DepartmentRepo deptrepo;
	
	
	@Override
	public Department saveDepartment(Department dept) {
		// TODO Auto-generated method stub
		if(dept!=null)
		{
			return deptrepo.save(dept);
		}
		else {
			return null;
		}
	}

	@Override
	public List<Department> getAllDepartments() {
		// TODO Auto-generated method stub
		return deptrepo.findAll();
	}

	@Override
	public Department getDepartmentById(String deptid) {
		// TODO Auto-generated method stub
		
		Long did = Long.valueOf(deptid);
		
		Department dept = deptrepo.findById(did).get();
		
		if(dept!=null)
		{
			return dept;
		}
		else {
			return null;
		}
	}

	@Override
	public int updateDepartment(Department dept) {
		// TODO Auto-generated method stub
		return deptrepo.updateDepartmentById(dept.getDept_name(), dept.getCompany().getComp_id(), dept.getDept_id());
	}

}
