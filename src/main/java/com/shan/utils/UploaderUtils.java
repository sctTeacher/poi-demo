//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shan.utils;


import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploaderUtils {
    private static final Logger logger = LoggerFactory.getLogger(UploaderUtils.class);

    public UploaderUtils() {
    }

    public static String uploadFile(MultipartFile file, String filePath) {
        String fileName = file.getOriginalFilename();
        String newFileName = DateUtils.getInstance().dateFormat(new Date(System.currentTimeMillis()));
        String ext = "";
        if (!StringUtils.isEmpty(fileName)) {
            ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        //目标路径+当时时间 yyyy-MM-dd
        String targetFilePath = filePath + File.separator + DateUtils.getInstance().dateFormat10(System.currentTimeMillis());
        logger.info("上传路径{}", targetFilePath);
        System.out.println();
        File targetFile = new File(targetFilePath, newFileName + ext);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(targetFile);
        } catch (IllegalStateException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return targetFile.getPath();
    }
}
