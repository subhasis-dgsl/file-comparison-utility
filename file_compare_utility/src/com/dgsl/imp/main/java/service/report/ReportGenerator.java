/**
 * 
 */
package com.dgsl.imp.main.java.service.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.dgsl.imp.main.java.bo.FileDetail;

//import org.apache.poi.ss.usermodel.ComparisonOperator;

/**
 * @author subhasis.swain
 *
 */
public class ReportGenerator {

	public ReportGenerator() {

	}

	HSSFWorkbook my_workbook = new HSSFWorkbook();
	HSSFSheet my_sheet = my_workbook.createSheet("Autofilter");
	private static int header = 1;
	private int row = 1;

	/* Create Test Data */
	/* Add header rows */

	public void createReport(List<FileDetail> fileDetail) throws IOException {

		if (header == 1) {
			Row row0 = my_sheet.createRow(0);
			row0.createCell(0).setCellValue("File Name");
			row0.createCell(1).setCellValue("File Path");
			row0.createCell(2).setCellValue("Status");
			header++;
		}
		/* Add test data */
		for (FileDetail fileDetails2 : fileDetail) {
			Row row1 = my_sheet.createRow(row);
			row1.createCell(0).setCellValue(fileDetails2.getFileName());
			row1.createCell(1).setCellValue(fileDetails2.getFilePath());
			row1.createCell(2).setCellValue(fileDetails2.getStatus());
			++row;
		}

		/*
		 * Row row1 = my_sheet.createRow(1);
		 * row1.createCell(0).setCellValue("Q1");
		 * row1.createCell(1).setCellValue("A");
		 * row1.createCell(2).setCellValue(new Double(10));
		 * 
		 * Row row2 = my_sheet.createRow(2);
		 * row2.createCell(0).setCellValue("Q1");
		 * row2.createCell(1).setCellValue("B");
		 * row2.createCell(2).setCellValue(new Double(15));
		 * 
		 * Row row3 = my_sheet.createRow(3);
		 * row3.createCell(0).setCellValue("Q2");
		 * row3.createCell(1).setCellValue("A");
		 * row3.createCell(2).setCellValue(new Double(23));
		 * 
		 * Row row4 = my_sheet.createRow(4);
		 * row4.createCell(0).setCellValue("Q2");
		 * row4.createCell(1).setCellValue("C");
		 * row4.createCell(2).setCellValue(new Double(24));
		 */

		// my_sheet.setAutoFilter(CellRangeAddress.valueOf("A1:C5"));
		/* Write changes to the workbook */

		FileOutputStream out = new FileOutputStream(new File(
				"D:\\temp3\\excel_auto_filter.xls"));
		my_workbook.write(out);
		out.close();
	}

}
