package com.shan.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPoiUtils {
    private static final String REGEX = "[a-zA-Z]";
    public static final int EXPORT_ROWS_MAX_INDEX = 65535;
    private String title;
    private String sheetName;
    private String[] FixedRowname;
    private int[] FixedRownameColumnWidth;
    private List<List<Object>> dataList;
    HttpServletResponse response;

    private static String convertToMethodName(String attribute, Class<?> objClass, boolean isSet) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(attribute);
        StringBuilder sb = new StringBuilder();
        if (isSet) {
            sb.append("set");
        } else {
            try {
                Field attributeField = objClass.getDeclaredField(attribute);
                if (attributeField.getType() != Boolean.TYPE && attributeField.getType() != Boolean.class) {
                    sb.append("get");
                } else {
                    sb.append("is");
                }
            } catch (SecurityException var7) {
                var7.printStackTrace();
            } catch (NoSuchFieldException var8) {
                var8.printStackTrace();
            }
        }

        if (attribute.charAt(0) != '_' && m.find()) {
            sb.append(m.replaceFirst(m.group().toUpperCase()));
        } else {
            sb.append(attribute);
        }

        return sb.toString();
    }

    public static HSSFWorkbook create(Map<Integer, String> titleMap, Map<Integer, String[]> dataMap, int[] dates) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet");
        sheet.setDefaultColumnWidth(15);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFRow row = sheet.createRow(0);
        style.setAlignment((short) 2);
        HSSFCell cell = null;

        int i;
        for (i = 0; i < titleMap.size(); ++i) {
            cell = row.createCell(i);
            cell.setCellValue((String) titleMap.get(i));
            cell.setCellStyle(style);
            if (dataMap.get(i) != null) {
                if (((String[]) dataMap.get(i)).length > 10) {
                    sheet = validationHidden(sheet, wb, (String[]) dataMap.get(i), 1, 100, i, i);
                } else {
                    sheet = validation(sheet, (String[]) dataMap.get(i), 1, 65535, i, i);
                }
            }
        }

        if (dates != null && dates.length > 0) {
            for (i = 0; i < dates.length; ++i) {
                dataformat(wb, sheet, dates[i]);
            }
        }

        return wb;
    }

    public static void create(OutputStream out, Map<Integer, String> titleMap, Map<Integer, String[]> dataMap, int[] dates) throws IOException {
        create(titleMap, dataMap, dates).write(out);
    }

    public static void dataformat(HSSFWorkbook wb, HSSFSheet sheet, int Col) {
        HSSFCellStyle hcs = wb.createCellStyle();
        hcs.setDataFormat(wb.createDataFormat().getFormat("yyyy/m/d h:mm"));
        sheet.setDefaultColumnStyle(Col, hcs);
    }

    private static List<Object> doImportExcel(String originUrl, int startRow, int endRow, boolean showInfo, Class<?> clazz) throws IOException {
        File file = new File(originUrl);
        if (!file.exists()) {
            throw new IOException("????????????" + file.getName() + "Excel??????????????????");
        } else {
            HSSFWorkbook wb = null;
            FileInputStream fis = null;
            ArrayList rowList = new ArrayList();

            try {
                fis = new FileInputStream(file);
                wb = new HSSFWorkbook(fis);
                Sheet sheet = wb.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();
                if (lastRowNum > 0) {
                    out("\n?????????????????????" + sheet.getSheetName() + "???????????????", showInfo);
                }

                Row row = null;

                for (int i = startRow; i <= lastRowNum + endRow; ++i) {
                    row = sheet.getRow(i);
                    if (row != null) {
                        rowList.add(row);
                        out("???" + (i + 1) + "??????", showInfo, false);

                        for (int j = 0; j < row.getLastCellNum(); ++j) {
                            String value = getCellValue(row.getCell(j));
                            if (!value.equals("")) {
                                out(value + " | ", showInfo, false);
                            }
                        }

                        out("", showInfo);
                    }
                }
            } catch (IOException var18) {
                var18.printStackTrace();
            } finally {
                fis.close();
            }

            return returnObjectList(rowList, clazz);
        }
    }

    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        result = DateUtils.getInstance().dateFormat3(date);
                    } else {
                        result = cell.getNumericCellValue();
                    }
                    break;
                case 1:
                    result = cell.getStringCellValue();
                    break;
                case 2:
                    result = cell.getCellFormula();
                case 3:
                default:
                    break;
                case 4:
                    result = cell.getBooleanCellValue();
                    break;
                case 5:
                    result = cell.getErrorCellValue();
            }
        }

        return result.toString();
    }

    public static List<?> importExcel(String originUrl, int startRow, int endRow, Class<?> clazz) throws IOException {
        boolean showInfo = true;
        return doImportExcel(originUrl, startRow, endRow, showInfo, clazz);
    }

    public static HttpServletResponse out(HttpServletResponse response, String filename) throws UnsupportedEncodingException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + new String(filename.getBytes("UTF-8"), "ISO8859-1"));
        return response;
    }

    private static void out(String info, boolean showInfo) {
        if (showInfo) {
            System.out.print(info + (showInfo ? "\n" : ""));
        }

    }

    private static void out(String info, boolean showInfo, boolean nextLine) {
        if (showInfo) {
            if (nextLine) {
                System.out.print(info + (showInfo ? "\n" : ""));
            } else {
                System.out.print(info);
            }
        }

    }

    private static List<Object> returnObjectList(List<Row> rowList, Class<?> clazz) {
        List<Object> objectList = null;
        Object obj = null;
        String attribute = null;
        String value = null;
        boolean var6 = false;

        try {
            objectList = new ArrayList();
            Field[] declaredFields = clazz.getDeclaredFields();
            Iterator var8 = rowList.iterator();

            while (var8.hasNext()) {
                Row row = (Row) var8.next();
                int j = 0;
                obj = clazz.newInstance();
                Field[] var10 = declaredFields;
                int var11 = declaredFields.length;

                for (int var12 = 0; var12 < var11; ++var12) {
                    Field field = var10[var12];
                    attribute = field.getName().toString();
                    value = getCellValue(row.getCell(j));
                    setAttrributeValue(obj, attribute, value);
                    ++j;
                }

                objectList.add(obj);
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        return objectList;
    }

    private static void setAttrributeValue(Object obj, String attribute, String value) {
        String method_name = convertToMethodName(attribute, obj.getClass(), true);
        Method[] methods = obj.getClass().getMethods();
        Method[] var5 = methods;
        int var6 = methods.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            if (method.getName().equals(method_name)) {
                Class[] parameterC = method.getParameterTypes();

                try {
                    if (parameterC[0] != Integer.TYPE && parameterC[0] != Integer.class) {
                        if (parameterC[0] != Float.TYPE && parameterC[0] != Float.class) {
                            if (parameterC[0] != Double.TYPE && parameterC[0] != Double.class) {
                                if (parameterC[0] != Byte.TYPE && parameterC[0] != Byte.class) {
                                    if (parameterC[0] != Boolean.TYPE && parameterC[0] != Boolean.class) {
                                        if (parameterC[0] == Date.class) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = null;

                                            try {
                                                if (!StringUtils.isEmpty(value)) {
                                                    date = sdf.parse(value);
                                                }
                                            } catch (Exception var13) {
                                                var13.printStackTrace();
                                            }

                                            method.invoke(obj, date);
                                        } else {
                                            method.invoke(obj, parameterC[0].cast(value));
                                        }
                                        break;
                                    }

                                    method.invoke(obj, Boolean.valueOf(value));
                                    break;
                                }

                                method.invoke(obj, Byte.valueOf(value));
                                break;
                            }

                            method.invoke(obj, Double.valueOf(value));
                            break;
                        }

                        method.invoke(obj, Float.valueOf(value));
                        break;
                    }

                    value = value.substring(0, value.lastIndexOf("."));
                    method.invoke(obj, Integer.valueOf(value));
                    break;
                } catch (IllegalArgumentException var14) {
                    var14.printStackTrace();
                } catch (IllegalAccessException var15) {
                    var15.printStackTrace();
                } catch (InvocationTargetException var16) {
                    var16.printStackTrace();
                } catch (SecurityException var17) {
                    var17.printStackTrace();
                }
            }
        }

    }

    public static HSSFSheet validation(HSSFSheet sheet, String[] strFormulaArray, int firstRow, int endRow, int firstCol, int endCol) {
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(strFormulaArray);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        HSSFDataValidation validation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(validation);
        return sheet;
    }

    public static HSSFSheet validationHidden(HSSFSheet sheet, HSSFWorkbook wb, String[] strFormulaArray, int firstRow, int endRow, int firstCol, int endCol) {
        String hiddenSheet = "hidden" + firstCol;
        HSSFSheet hidden = wb.createSheet(hiddenSheet);
        int i = 0;

        for (int length = strFormulaArray.length; i < length; ++i) {
            hidden.createRow(endRow + i).createCell(firstCol).setCellValue(strFormulaArray[i]);
        }

        HSSFName name = wb.createName();
        name.setNameName(hiddenSheet);
        name.setRefersToFormula(hiddenSheet + "!A1:A" + (strFormulaArray.length + endRow));
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheet);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);
        wb.setSheetHidden(1, true);
        sheet.addValidationData(validation);
        return sheet;
    }

    public MyPoiUtils(String title, String[] fixedRowname, List<List<Object>> dataList) {
        this(title, "sheet", fixedRowname, null, dataList);
    }

    public MyPoiUtils(String title, String[] fixedRowname, int[] fixedRownameColumnWidth, List<List<Object>> dataList) {
        this(title, "sheet", fixedRowname, fixedRownameColumnWidth, dataList);
    }

    public MyPoiUtils(String title, String sheetName, String[] fixedRowname, int[] fixedRownameColumnWidth, List<List<Object>> dataList) {
        this.dataList = new ArrayList();
        this.title = title;
        this.sheetName = sheetName;
        this.FixedRowname = fixedRowname;
        this.dataList = dataList;
        this.FixedRownameColumnWidth = fixedRownameColumnWidth;
    }

    public HSSFWorkbook export() throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(this.sheetName);
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            HSSFCellStyle style = this.getStyle(workbook);
            int fixedColumnNum = this.FixedRowname.length;
            HSSFRow rowRowTitle = sheet.createRow(0);
            if (this.FixedRownameColumnWidth == null) {
                sheet.setDefaultColumnWidth(10);
            } else {
                int fixedRownameColumnWidthNum = this.FixedRownameColumnWidth.length;
                for (int i = 0; i < fixedRownameColumnWidthNum; ++i) {
                    sheet.setColumnWidth(i, this.FixedRownameColumnWidth[i]);
                }
            }
            for (int i = 0; i < fixedColumnNum; ++i) {
                HSSFCell titleCell = rowRowTitle.createCell(i);
                titleCell.setCellType(1);
                if (i == 0) {
                    titleCell.setCellValue(this.title);
                }

                titleCell.setCellStyle(columnTopStyle);
            }

            sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (fixedColumnNum - 1)));
            HSSFRow rowRowName = sheet.createRow(1);

            int i;
            for (i = 0; i < fixedColumnNum; ++i) {
                HSSFCell fixedCellRowName = rowRowName.createCell(i);
                fixedCellRowName.setCellType(1);
                HSSFRichTextString text = new HSSFRichTextString(this.FixedRowname[i]);
                fixedCellRowName.setCellValue(text);
                fixedCellRowName.setCellStyle(columnTopStyle);
            }

            for (i = 0; i < this.dataList.size(); ++i) {
                List<Object> obj = (List) this.dataList.get(i);
                HSSFRow row = sheet.createRow(i + 2);

                for (int j = 0; j < obj.size(); ++j) {
                    HSSFCell cell = row.createCell(j, 0);
                    cell.setCellValue(String.valueOf(obj.get(j)));
                    cell.setCellStyle(style);
                }
            }

            return workbook;
        } catch (Exception var13) {
            var13.printStackTrace();
            return null;
        }
    }

    public void export(OutputStream out) throws Exception {
        this.export().write(out);
    }

    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight((short) 700);
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom((short) 1);
        style.setBottomBorderColor((short) 8);
        style.setBorderLeft((short) 1);
        style.setLeftBorderColor((short) 8);
        style.setBorderRight((short) 1);
        style.setRightBorderColor((short) 8);
        style.setBorderTop((short) 1);
        style.setTopBorderColor((short) 8);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment((short) 2);
        style.setVerticalAlignment((short) 1);
        return style;
    }

    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("Courier New");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom((short) 1);
        style.setBottomBorderColor((short) 8);
        style.setBorderLeft((short) 1);
        style.setLeftBorderColor((short) 8);
        style.setBorderRight((short) 1);
        style.setRightBorderColor((short) 8);
        style.setBorderTop((short) 1);
        style.setTopBorderColor((short) 8);
        style.setFont(font);
        style.setWrapText(false);
        style.setAlignment((short) 2);
        style.setVerticalAlignment((short) 1);
        return style;
    }
}

