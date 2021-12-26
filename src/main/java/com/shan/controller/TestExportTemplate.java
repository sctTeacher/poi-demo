package com.shan.controller;

import com.shan.entity.common.ExportDefinition;
import com.shan.utils.PoiUtils;
import com.shan.utils.Template2Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * 使用poi进行模板导出 含下拉框数据
 */
@Controller
@RequestMapping("test")
public class TestExportTemplate {

    /**
     * 含有普通下拉框数据和级联下拉框的模板下载
     *
     * @param response
     */
    @GetMapping("testExportTemplateCascade")
    public void testExportTemplateCascade(HttpServletResponse response) {
        try {
            //存下拉数据
            List<Map<String, Object>> dictList = new ArrayList<>();
            //存级联数据关系
            List<List<ExportDefinition>> edListList = new ArrayList<>();
            Map<Integer, String> tableHeader = new HashMap<>();
            String[] tableHeaderArray = {"组织", "人员", "类型", "有效时间"};
            for (int i = 0; i < tableHeaderArray.length; i++) {
                tableHeader.put(i, tableHeaderArray[i]);
            }
            //存下拉框的值
            Map<Integer, String[]> sequenceMap = new HashMap<>();
            //为了格式化日期 指定日期列
            int[] dates = {3};
            System.out.println(Arrays.toString(dates));
            // 构建级联下拉框数据
            initData(dictList, edListList);
            //构建普通下拉数据
            String[] typeArray = {"类型一", "类型二", "类型三"};
            sequenceMap.put(2, typeArray);

            response = PoiUtils.out(response, "test1.xls");
            String fn = "test1.xls";
            //解决乱码
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fn.getBytes("GBK"), "ISO8859-1"));

            OutputStream os = null;
            try {
                os = response.getOutputStream();
                Template2Utils.createRelate(os, tableHeader, sequenceMap, dates, edListList, dictList);
                os.flush();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(List<Map<String, Object>> dictList, List<List<ExportDefinition>> edListList) {
        // 举例 一级组织  二级对应组织下的人员
        //存一级下拉数据 二级下拉数据
        Map<String, Object> dict = new HashMap<>();
        //存 二级下拉数据
        Map<String, List<String>> subMap = new HashMap<>();
        //模拟组织数据
        List<String> list = new ArrayList<>();
        list.add("组织一");
        list.add("组织二");
        //模拟人员数据
        List<String> userlist1 = new ArrayList<>();
        userlist1.add("张三");
        userlist1.add("李四");
        List<String> userlist2 = new ArrayList<>();
        userlist2.add("王五");
        subMap.put("组织一", userlist1);
        subMap.put("组织二", userlist2);

        dict.put("org", list);
        dict.put("user", subMap);
        dictList.add(dict);

        // 创建数据关系
        List<ExportDefinition> edList = new ArrayList<>();
        edList.add(new ExportDefinition("", "org", "org", "user", "user"));
        edList.add(new ExportDefinition("", "user", "user", "", ""));
        edListList.add(edList);

    }


    /**
     * 含有下拉框数据的模板下载
     *
     * @param response
     */
    @GetMapping("testExportTemplateSelect")
    public void testExportTemplateSelect(HttpServletResponse response) {
        try {

            Map<Integer, String> tableHeader = new HashMap<>();
            String[] tableHeaderArray = {"组织", "应用软件", "类型", "有效时间"};
            for (int i = 0; i < tableHeaderArray.length; i++) {
                tableHeader.put(i, tableHeaderArray[i]);
            }
            //存下拉框的值 (单元格位置:1,2)
            Map<Integer, String[]> sequenceMap = new HashMap<>();

            //软件下拉数据
            String[] appArray = {"软件一", "软件二"};
            sequenceMap.put(1, appArray);

            //类型下拉数据
            String[] typeArray = {"类型一", "类型二", "类型三"};
            sequenceMap.put(2, typeArray);

            //为了格式化日期 指定日期列(单元格位置:3)
            int[] dates = {3};
            System.out.println(Arrays.toString(dates));

            response = PoiUtils.out(response, "test2.xls");
            String fn = "test2.xls";
            //解决乱码
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fn.getBytes("GBK"), "ISO8859-1"));

            OutputStream os = null;
            try {
                os = response.getOutputStream();
                PoiUtils.create(os, tableHeader, sequenceMap, null);
                os.flush();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
