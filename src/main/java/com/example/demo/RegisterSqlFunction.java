package com.example.demo;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class RegisterSqlFunction implements MetadataBuilderContributor {

	@Override
	public void contribute(MetadataBuilder metadataBuilder) {
		// TODO Auto-generated method stub
		metadataBuilder.applySqlFunction("group_concat", new StandardSQLFunction("group_concat",StandardBasicTypes.STRING ));	
	}
}
