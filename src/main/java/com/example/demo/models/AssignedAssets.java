package com.example.demo.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name="tbl_assigned_assets")
public class AssignedAssets {

	
	@Id
	@SequenceGenerator(name="assigned_asset_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "assigned_asset_seq")
	private Long assigned_asset_id;
	
	
	@ManyToOne(targetEntity = Employee.class , cascade = {CascadeType.MERGE})
	@JoinColumn(name="emp_id",referencedColumnName = "emp_id")
	private Employee employee;
	
	
	@ManyToOne(targetEntity = Assets.class,cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name="asset_id", referencedColumnName = "asset_id")
	private Assets asset;
	
	private String assign_date;
	
	private String assign_time;
	
	@Transient 
	private String assigned_assets;
	
	@Transient 
	private List<String> ass_assets;
	
	@Transient
	private List<String> assigned_asset_types;
	
	@Transient
	private String assigned;
	
	@Transient
	private Long asset_id;
	
	@Transient
	private Long emp_id;
	
	@Transient
	private String multi_assets;

	public AssignedAssets(Long eid,String aname) {} 
}
