package com.shan.controller;

import com.shan.entity.po.OrgImportPo;
import com.shan.utils.PoiUtils;
import com.shan.utils.UploaderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 使用poi 上传并导入数据
 */
@RestController
@RequestMapping("test")
public class TestImportData {

    @PostMapping("/importTest")
    public String importAuthorization(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith("xls")) {
            return "类型不符";
        }
        //上传    上传的位置 可根据服务器位置 类型灵活指定
        String path = UploaderUtils.uploadFile(file, "D:\\Download");
        List<?> importExcel;
        try {
            //读取数据填充到实体中
            importExcel = PoiUtils.importExcel(path, 1, 0, OrgImportPo.class);
            if (CollectionUtils.isEmpty(importExcel)) {
                return "SUCCESS";
            }
            List<OrgImportPo> listDate = (List<OrgImportPo>) importExcel;
            for (OrgImportPo entity : listDate) {
                System.out.println(entity.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }


}
