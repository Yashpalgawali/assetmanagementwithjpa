package com.example.demo.exporttoexcel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.models.AssetAssignHistory;

public class ExportAssetAssignHistory {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private List<AssetAssignHistory> ahist;
	
	public ExportAssetAssignHistory(List<AssetAssignHistory> ahist)
	{
		this.ahist = ahist;
		workbook = new XSSFWorkbook();
	}
	
	public void writeHeaderLine() {
		sheet = workbook.createSheet("Asset Assigned History");
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		
		style.setFont(font);
	
		createCell(row,0,"Sr No.",style);
		createCell(row,1,"Asset",style);
		createCell(row,2,"Asset Type",style);
		createCell(row,3,"Model Number",style);
		createCell(row,4,"Date",style);
		createCell(row,5,"Operation",style);
		createCell(row,6,"Employee",style);
		
	}
	
	public void createCell(Row row,int columnCount, Object value,CellStyle style)
	{
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
	}
	
	public void writeDataLines() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		int sr=1,cn=1;
		
		
		for(AssetAssignHistory hist : ahist)
		{
			Row row = sheet.createRow(rowCount++);
			
			int columnCount = 0;
			
			createCell(row,columnCount++, sr++ ,style);
			createCell(row,columnCount++, hist.getAsset().getAsset_name() ,style);
			createCell(row,columnCount++, hist.getAsset().getAtype().getType_name() ,style);
			createCell(row,columnCount++, hist.getAsset().getModel_number() ,style);
			createCell(row,columnCount++, hist.getOperation_date() ,style);
			createCell(row,columnCount++, hist.getOperation() ,style);
			if(cn==1)
			{
				createCell(row,columnCount++, hist.getEmployee().getEmp_name() ,style);
				cn++;
			}
		}
	}
	
	public void export(HttpServletResponse response)throws IOException {
		writeHeaderLine();
		writeDataLines();
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
	}
	
}
