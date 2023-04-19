package com.example.demo.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_asset_assign_history")
public class AssetAssignHistory {

	@Id
	@SequenceGenerator(name = "hist_seq" , allocationSize = 1 , initialValue = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hist_seq")
	private Long hist_id;
	
	
	@ManyToOne(targetEntity = Assets.class, cascade = {CascadeType.MERGE , CascadeType.REFRESH, CascadeType.REMOVE})
	@JoinColumn(name="asset_id",referencedColumnName = "asset_id")
	private Assets asset;
	
	@ManyToOne(targetEntity = Employee.class , cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE })
	@JoinColumn(name="emp_id",referencedColumnName = "emp_id")
	private Employee employee;
	
	private String operation_date;
	
	private String operation_time;
	
	private String operation;
}
