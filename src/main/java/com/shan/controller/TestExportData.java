package com.shan.controller;

import com.shan.entity.po.OrgImportPo;
import com.shan.utils.DateUtils;
import com.shan.utils.MyPoiUtils;
import com.shan.utils.PoiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 使用poi导出列表工具类
 */
@Controller
@RequestMapping("test")
public class TestExportData {
    private final Logger logger = LoggerFactory.getLogger(TestExportData.class);

    /**
     * 导出列表
     *
     * @param response
     */
    @GetMapping("testExportList")
    public void testExportList(HttpServletResponse response) {
        OutputStream os = null;
        try {
            //标题
            String[] headers = {"组织", "应用软件", "类型"};
            //数据集合  模拟查询数据
            List<List<Object>> outList = new ArrayList<List<Object>>();
            for (int i = 0; i < 50; i++) {
                List<Object> inList = new ArrayList<Object>();
                inList.add("组织名" + i);
                inList.add("应用软件名" + i);
                inList.add("类型名" + i);
                outList.add(inList);
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            String fileName = "exportAccountDetaiBill.xls";
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            //采用默认导出样式  标题 标题头  数据集合
            //PoiUtils excelSoftWare = new PoiUtils( "账单明细_" + DateUtils.getInstance().dateFormat(new Date(), "yyyy-MM-dd"), headers, outList);
            //自定义宽度 参数  自定义每列宽度 标题 标题头  数据集合
            int[] FixedRownameColumnWidth = {10 * 256, 35 * 256, 25 * 256};
            MyPoiUtils excelSoftWare = new MyPoiUtils("账单明细_" + DateUtils.getInstance().dateFormat(new Date(), "yyyy-MM-dd"), headers, FixedRownameColumnWidth, outList);
            os = response.getOutputStream();
            excelSoftWare.export(os);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("/exportAccountDetaiBill 导出出现异常：", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
