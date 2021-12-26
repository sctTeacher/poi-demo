package com.shan.service.impl;

import com.shan.entity.po.CloudFile;
import com.shan.file.ResumeUploader;
import com.shan.service.UploaderService;
import com.shan.utils.Md5Util;
import com.shan.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Service
public class UploaderServiceImpl implements UploaderService {

    @Value("${spring.uploader.rootdir}")
    private String fileUploadPath;

    @Autowired
    private ResumeUploader fileOperation;


    @Override
    public Boolean checkChunk(String md5Value, Integer chunk, String moduleName, String fileTypeKey) {
        String path = specifiedDirectory(fileTypeKey) + moduleName + File.separator + fileTypeKey + File.separator;
        return fileOperation.checkChunk(path, md5Value, chunk);
    }

    @Override
    public Boolean uploadChunk(MultipartFile file, String md5Value, Integer chunk, String moduleName, String fileTypeKey) {
        String path = specifiedDirectory(fileTypeKey) + moduleName + File.separator + fileTypeKey + File.separator;
        return fileOperation.upload(file, path, md5Value, chunk);
    }

    @Override

    public String merge(Integer chunkCount, String md5Value, String fileName, String moduleName, String fileTypeKey) {
        String path = specifiedDirectory(fileTypeKey) + moduleName + File.separator + fileTypeKey + File.separator;

        // 目录下如果存在相同名称的文件，对文件名称做修改   xxx(1).txt  xxx(2).txt
        fileName = this.verifyRepeatFile(path, fileName, 0);
        Boolean merge = fileOperation.merge(path, path, md5Value, chunkCount, fileName);
        if (merge) {
            String filePath = path + fileName;
            CloudFile cloudFile = new CloudFile();
            cloudFile.setId(UUIDGenerator.getUUID());
            cloudFile.setMd5(Md5Util.fastMD5(new File(filePath)));
            cloudFile.setName(fileName.substring(0, fileName.lastIndexOf(".")));
            cloudFile.setType(fileName.substring(fileName.lastIndexOf(".")));
            cloudFile.setPath(filePath);
            cloudFile.setFileSize(new File(filePath).length());
            cloudFile.setGmtCreate(new Date());
            //存数据库
            // int save = cloudFileMapper.save(cloudFile);
            int save = 1;
            if (save > 0) {
                return cloudFile.getId();
            }
        }
        return null;
    }

    /**
     * 根据文件类型指定文件目录
     *
     * @param fileTypeKey 文件类型
     */
    @Override
    public String specifiedDirectory(String fileTypeKey) {
        // 一、通用管理文件根路径
        String fPath = fileUploadPath;
        return fPath.endsWith("/") ? fPath : fPath + "/";
    }


    private String verifyRepeatFile(String path, String fileName, int i) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        String tempFileName;
        if (i != 0) {
            tempFileName = String.format("%s%s%d%s", name, "(", i, ")");
        } else {
            tempFileName = name;
        }
        File file = new File(path + tempFileName + suffix);
        if (file.exists()) {
            return verifyRepeatFile(path, name + suffix, ++i);
        }
        return tempFileName + suffix;
    }

}
