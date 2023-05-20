package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AssignedAssets;
import com.example.demo.models.Employee;
import com.example.demo.repository.AssignedAssetsRepo;
import com.example.demo.repository.EmployeeRepo;

@Service("empserv")
public class EmployeeServImpl implements EmployeeService {

	@Autowired
	EmployeeRepo emprepo;
	
	@Autowired
	AssignedAssetsRepo assignassetrepo;
	
	@Override
	public Employee saveEmployee(Employee emp) {
		// TODO Auto-generated method stub
	
		if(emp!=null)
		{
			return emprepo.save(emp);
		}
		else {
			return null;
		}
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return emprepo.findAll();
	}

	@Override
	public Employee getEmployeeById(String empid) {
		// TODO Auto-generated method stub
		if(empid!=null)
		{
			Long eid = Long.valueOf(empid);
			return emprepo.findById(eid).get();
		}
		else {
			return null;
		}
	}

	@Override
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		
		String new_assets = emp.getMulti_assets();
		List.of(new_assets);
		
		List<AssignedAssets> assigned_assets = assignassetrepo.getAllAssignedAssetsByEmpId(emp.getEmp_id());
		
		return 0;
	}
}
