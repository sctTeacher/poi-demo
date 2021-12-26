package com.shan.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

import com.shan.entity.common.ExportDefinition;
import com.shan.entity.common.RowCellIndex;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;



public class LinkagePoi2Utils {
	public static void createDataValidateSubList(Sheet sheet, ExportDefinition ed) {
        int rowIndex = ed.getRowIndex();
        CellRangeAddressList cal;
        DVConstraint constraint;
        CellReference cr;
        DataValidation dataValidation;
        for (int i = 0; i < 1000; i++) {
            int tempRowIndex = ++rowIndex;
            cal = new CellRangeAddressList(tempRowIndex, tempRowIndex, ed.getCellIndex(), ed.getCellIndex());
            cr = new CellReference(rowIndex, ed.getCellIndex() - 1, true, true);
            constraint = DVConstraint.createFormulaListConstraint("INDIRECT(" + cr.formatAsString() + ")");
            dataValidation = new HSSFDataValidation(cal, constraint);
            dataValidation.setSuppressDropDownArrow(false);
            dataValidation.createPromptBox("操作提示", "请选择下拉选中的值");
            dataValidation.createErrorBox("错误提示", "请从下拉选中选择，不要随便输入");
            sheet.addValidationData(dataValidation);
        }
    }

    /**
     * 设置数据有效性
     * @param edList
     * @param sheet
     */
    private static void setDataValidation(List<ExportDefinition> edList, Sheet sheet) {
        for (ExportDefinition ed : edList) {
            if (ed.isValidate()) {// 说明是下拉选
                DVConstraint constraint = DVConstraint.createFormulaListConstraint(ed.getField());
                if (null == ed.getRefName()) {// 说明是一级下拉选
                    createDataValidate(sheet, ed, constraint);
                } else {// 说明是二级下拉选
                    createDataValidateSubList(sheet, ed);
                }
            }
        }
    }

    /**
     * @param sheet
     * @param ed
     * @param constraint
     */
    private static void createDataValidate(Sheet sheet, ExportDefinition ed, DVConstraint constraint) {
        CellRangeAddressList regions = new CellRangeAddressList(ed.getRowIndex() + 1, ed.getRowIndex() + 100, ed.getCellIndex(), ed.getCellIndex());
        DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.setSuppressDropDownArrow(false);
        // 设置提示信息
        dataValidation.createPromptBox("操作提示", "请选择下拉选中的值");
        // 设置输入错误信息
        dataValidation.createErrorBox("错误提示", "请从下拉选中选择，不要随便输入");
        sheet.addValidationData(dataValidation);
    }

    /**
     * 创建数据字典sheet页
     * @param edList
     * @param wb
     */
	@SuppressWarnings("unchecked")
	private static void createDictSheet(List<ExportDefinition> edList, Workbook wb, String dictSheetName, Map<String, Object> dic) {
        Sheet sheet = wb.createSheet(dictSheetName);
        sheet.setDefaultColumnWidth (15);// 设置单元格宽度
        wb.setSheetHidden(wb.getSheetIndex(dictSheetName), 1);

        RowCellIndex rci = new RowCellIndex(0, 0);
        for (ExportDefinition ed : edList) {
            String mainDict = ed.getMainDict();
            List<String> mainDictList = null;
            //第一列选择项
            if (null != mainDict && null == ed.getRefName()) {// 是第一个下拉选
                Object object = dic.get(mainDict);
                if (object instanceof ArrayList){
                    mainDictList = (List<String>) dic.get(mainDict);
                    if (mainDictList == null || mainDictList.size() == 0) {
                        continue;
                    }
                    String refersToFormula = createParentDictAndReturnRefFormula(sheet, rci, mainDictList, dictSheetName);
                    // 创建 命名管理
                    createName(wb, ed.getField(), refersToFormula);
                    ed.setValidate(true);
                }
            }

            //被联动选择项
            if (null != mainDict && null != ed.getSubDict() && null != ed.getSubField()) {// 联动时加载ed.getSubField()的数据
                ExportDefinition subEd = fiterByField(edList, ed.getSubField());// 获取需要级联的那个字段
                if (null == subEd) {
                    continue;
                }
                subEd.setRefName(ed.getPoint());// 保存主下拉选的位置
                subEd.setValidate(true);
                Map<String, List<String>> subDictListMap = (Map<String, List<String>>) dic.get(ed.getSubDict());

                rci.setRowIndex(1);
                rci.setCellIndex(0);
                for (String keys : mainDictList) {
                    List<String> values = subDictListMap.get(keys);
                    String refersToFormula = createSonDicAndReturnRefFormula(sheet, rci, values, dictSheetName);
                    // 创建 命名管理
                    createName(wb, keys, refersToFormula);

                    rci.setRowIndex(1);
                    rci.incrementCellIndexAndGet();
                }
            }
        }
    }

    /**
     * 纵向输出数据 -- 子节点
     *
     * @param sheet
     * @param rci
     * @param datas
     * @return
     */
    private static String createSonDicAndReturnRefFormula(Sheet sheet, RowCellIndex rci, List<String> datas, String dicSheetName) {
        Row row = sheet.getRow(rci.getRowIndex());
        if (row == null) {
            row = sheet.createRow(rci.getRowIndex());
            row.setHeightInPoints (20);// 行高
        }

        int startRow = rci.getRowIndex();
        int startCell = rci.getCellIndex();
        for (String dict : datas) {
            row.createCell(rci.getCellIndex()).setCellValue(new HSSFRichTextString(dict));
            rci.incrementRowIndexAndGet();
            row = sheet.getRow(rci.getRowIndex());
            if (row == null) {
                row = sheet.createRow(rci.getRowIndex());
                row.setHeightInPoints (20);// 行高
            }
        }
        rci.reduceRowIndexAndGet();

        int endRow = rci.getRowIndex();
        int endCell = rci.getCellIndex();
        String startName = new CellReference(dicSheetName, startRow, startCell, true, true).formatAsString();
        String endName = new CellReference(endRow, endCell, true, true).formatAsString();
        String refersToFormula = startName + ":" + endName;
        rci.incrementRowIndexAndGet();
        return refersToFormula;
    }

    /**
     * 横向输出数据 -- 父节点
     *
     * @param sheet
     * @param rci
     * @param datas
     * @return
     */
    private static String createParentDictAndReturnRefFormula(Sheet sheet, RowCellIndex rci, List<String> datas, String dictSheetName) {
        Row row = sheet.createRow(rci.getRowIndex());
        row.setHeightInPoints (20);// 行高

        rci.setCellIndex(0);
        int startRow = rci.getRowIndex();
        int startCell = rci.getCellIndex();
        for (String dict : datas) {
            row.createCell(rci.getCellIndex()).setCellValue(new HSSFRichTextString(dict));
            rci.incrementCellIndexAndGet();
        }
        rci.reduceCellIndexAndGet();
        int endRow = rci.getRowIndex();
        int endCell = rci.getCellIndex();
        String startName = new CellReference(dictSheetName, startRow, startCell, true, true).formatAsString();
        String endName = new CellReference(endRow, endCell, true, true).formatAsString();
        String refersToFormula = startName + ":" + endName;
        rci.incrementRowIndexAndGet();
        return refersToFormula;
    }

    /**
     * @param wb
     * @param nameName        表示命名管理的名字
     * @param refersToFormula
     */
    private static void createName(Workbook wb, String nameName, String refersToFormula) {
        Name name = wb.createName();
        // setNameName不能添加首位是空格或数字的值,如果首位是数字,则加入下划线
        String reg = "^\\d|\\s$";
        if ((nameName.charAt(0)+"").matches(reg)) {
            nameName = "_"+nameName;
        }
        name.setNameName(nameName);
        name.setRefersToFormula(refersToFormula);
    }

    private static ExportDefinition fiterByField(List<ExportDefinition> edList, String field) {
        for (ExportDefinition ed : edList) {
            if (Objects.equals(ed.getField(), field)) {
                return ed;
            }
        }
        return null;
    }

    /**
     * 生成联动数据列
     * @param edList
     * @param wb
     * @param startRow 数据开始存放的行
     * @param startCell 数据开始存放的列
     * @param targetSheetName
     * @return
     */
    private static Sheet createExportSheet(List<ExportDefinition> edList, Workbook wb, int startRow, int startCell, String targetSheetName) {
        //获取需要加入联动的sheet
        Sheet sheet = wb.getSheet(targetSheetName);
        if (sheet == null) {
            sheet = wb.createSheet(targetSheetName);
        }
        //初始化位置
        RowCellIndex rci = new RowCellIndex(startRow, startCell);

        Row row = sheet.getRow(rci.getRowIndex());
        if (row == null) {
            //创建行
            row = sheet.createRow(rci.getRowIndex());
            row.setHeightInPoints (20);// 行高
        }

        CellReference cr = null;
        Cell cell = null;
        for (ExportDefinition ed : edList) {
            //从顶格开始
            //创建单元格
            cell = row.getCell(rci.getCellIndex());
            if(cell == null){
                cell = row.createCell(rci.getCellIndex());
                if(ed.getTitle() != null &&  ed.getTitle().trim() != ""){
                    cell.setCellValue(new HSSFRichTextString(ed.getTitle()));
                }
            }
            ed.setRowIndex(rci.getRowIndex());
            ed.setCellIndex(rci.getCellIndex());
            //将单元格坐标映射到excel坐标
            cr = new CellReference(ed.getRowIndex() + 1, ed.getCellIndex(), true, true);
            ed.setPoint(cr.formatAsString());
            rci.incrementCellIndexAndGet();

           
        }
        return sheet;
    }

    /**
     * 创建字典sheet并绑定数据下拉
     * @param edList
     * @param startRow
     * @param startCell
     * @param targetSheetName
     * @param dictSheetName
     * @param dic
     */
    public static HSSFWorkbook createData(HSSFWorkbook wb,List<ExportDefinition> edList, int startRow, int startCell, String targetSheetName, String dictSheetName, Map<String, Object> dic){

        // 1.生成导出模板
        Sheet sheet = createExportSheet(edList, wb, startRow, startCell,targetSheetName);

        // 2.创建数据字典sheet页
        createDictSheet(edList, wb, dictSheetName, dic);

        // 3.设置数据有效性
        setDataValidation(edList, sheet);

        return wb;
    }
    
    public static void main(String[] art){
    	// 1.准备需要生成excel模板的数据
        List<ExportDefinition> edList = new ArrayList<>();
        edList.add(new ExportDefinition("建筑", "Building", "Building", "Room", "Room"));
        edList.add(new ExportDefinition("场室", "Room", "Room", "", ""));
       //生成导出模板
        HSSFWorkbook wb = new HSSFWorkbook();
        //wb = LinkagePoiUtils.createData(wb,edList,0,0,"Sheet1", "dict_data",null);

        // 5.保存excel到本地
        OutputStream os;
		try {
			os = new FileOutputStream("D://4.xls");
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
