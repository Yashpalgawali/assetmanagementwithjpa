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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_assets")
public class Assets {

	@Id
	@SequenceGenerator(name="asset_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "asset_seq")
	private Long asset_id;
	
	private String asset_name;
	
	private String asset_number;
	
	private String model_number;
	
	private String quantity;
	
	@Transient
	private Long count;
	
	
	
	@ManyToOne(targetEntity = AssetType.class, cascade = {CascadeType.MERGE,CascadeType.REMOVE, CascadeType.REFRESH})
	@JoinColumn(name="type_id",referencedColumnName = "type_id")
	private AssetType atype;

	public Assets(Long asset_id, Long count) {
		super();
		this.asset_id = asset_id;
		this.count = count;
	}

		
}

