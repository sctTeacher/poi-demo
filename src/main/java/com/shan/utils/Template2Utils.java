package com.shan.utils;

import com.shan.entity.common.ExportDefinition;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template2Utils {
	public static final int EXPORT_ROWS_MAX_INDEX = 65535;

	public static HSSFWorkbook create(Map<Integer, String> titleMap, Map<Integer, String[]> dataMap, int[] dates) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet");
		sheet.setDefaultColumnWidth(15);

		HSSFCellStyle style = wb.createCellStyle();
		HSSFRow row = sheet.createRow(0);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = null;
		for (int i = 0; i < titleMap.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleMap.get(i));
			cell.setCellStyle(style);
			if (dataMap.get(i) != null) {
				if(dataMap.get(i).length>10) {
					sheet = validationHidden(sheet,wb,dataMap.get(i), 1, 100, i, i);
				}else{
					sheet = validation(sheet,dataMap.get(i), 1, EXPORT_ROWS_MAX_INDEX, i, i);
				}
			}
		}

		if (dates != null && dates.length > 0) {
			for (int i = 0; i < dates.length; i++) {
				dataformat(wb, sheet, dates[i]);
				sheet.createRow(1).createCell(dates[i]).setCellValue("2099-01-01");
			}
		}
		//sheet.createRow(1).createCell(5).setCellValue("2099-01-01");
		return wb;
	}

	public static void create(OutputStream out, Map<Integer, String> titleMap, Map<Integer, String[]> dataMap, int[] dates) throws IOException {
		create(titleMap, dataMap, dates).write(out);
	}
	public static void createRelate(OutputStream out, Map<Integer, String> titleMap, Map<Integer, String[]> dataMap, int[] dates, List<List<ExportDefinition>> edList, List<Map<String, Object>> dictList) throws IOException {
		HSSFWorkbook wb = create(titleMap, dataMap, dates);
		Map<String, Object> map = new HashMap<>();
		int startNumber = 0;
		for(int i=0;i<dictList.size();i++){
			map = dictList.get(i);
			if(!CollectionUtils.isEmpty(map)){
				wb = LinkagePoi2Utils.createData(wb, edList.get(i), 0, startNumber, "sheet", "dict_data"+i, map);
			}
			startNumber+=2;
		}
		wb.write(out);
		
	}

	public static HSSFSheet validationHidden(HSSFSheet sheet,HSSFWorkbook wb,String[] strFormulaArray, int firstRow, int endRow, int firstCol, int endCol) {
		String hiddenSheet = "hidden"+firstCol;
		HSSFSheet hidden = wb.createSheet(hiddenSheet); // 创建隐藏域
		for (int i = 0, length = strFormulaArray.length; i < length; i++) { // 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
			hidden.createRow(endRow+i).createCell(firstCol).setCellValue(strFormulaArray[i]);
        } 
		HSSFName name = wb.createName(); 
		name.setNameName(hiddenSheet);
		name.setRefersToFormula(hiddenSheet + "!A1:A" + (strFormulaArray.length + endRow)); // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheet); 
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol); 
		HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint); 
        wb.setSheetHidden(1, true); // 1隐藏、0显示
        sheet.addValidationData(validation);
		return sheet;
	}
	public static HSSFSheet validation(HSSFSheet sheet,String[] strFormulaArray, int firstRow, int endRow, int firstCol, int endCol) {
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(strFormulaArray);
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		HSSFDataValidation validation = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(validation);
		return sheet;
	}

	public static void dataformat(HSSFWorkbook wb, HSSFSheet sheet, int Col) {
		HSSFCellStyle hcs = wb.createCellStyle();
		// hcs.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-mm-dd
		// hh24:mm;ss"));
		// HSSFDataFormat format = wb.createDataFormat();
		// hcs.setDataFormat(format.getFormat("yyyy年m月d日"));
		hcs.setDataFormat(wb.createDataFormat().getFormat("yyyy/m/d h:mm"));
		sheet.setDefaultColumnStyle(Col, hcs);
	}
	public static HttpServletResponse out(HttpServletResponse response, String filename) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + new String(filename.getBytes("UTF-8"), "ISO8859-1"));
		return response;
	}

}
