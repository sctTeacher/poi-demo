package com.shan.controller;

import com.shan.service.UploaderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分片上传
 * 思想
 * 1. 先 checkUpload()  文件上传前校验 根据自己公司需求校验
 *2. checkChunk（）  检测指定分片是否存在   否 走uploadChunk（）分片上传方法  是走merge（）合成分片
 * 3. 分片上传时，会生成临时文件  必成分片是会把临时文件合成上传的真正文件，并删除临时文件
 *
 */
@RestController
@RequestMapping("uploader")
public class TestUploader {


    @Autowired
    private UploaderService uploaderService;

    /**
     * 文件上传前校验
     *
     * @param fileName 文件名称
     * @param size     文件大小 byte字节长度
     * @return
     */
    @PostMapping("/checkUpload")
    public String checkUpload(String fileName, String size) {
        //根据公司自己业务  做校验   如文件名是否喊特殊字符 是否为空 等
        return "SUCCESS";
    }



    /**
     * 检查指定分片是否存在
     *
     * @param md5Value    文件MD5值
     * @param chunk       第几片
     * @param moduleName  上传文件所在模块名称
     * @param fileTypeKey 文件类别   公司自己业务需求  会根据 模块名称+文件类别  定义上传目录
     * @return   存在 true  不存在false
     */
    @PostMapping("/checkChunk")
    public Boolean checkChunk(String md5Value, Integer chunk, String moduleName, String fileTypeKey) {
        Boolean bool = uploaderService.checkChunk(md5Value, chunk, moduleName, fileTypeKey);
        return bool;
    }


    /**
     * 分片上传
     *
     * @param file        文件对象
     * @param md5Value    文件MD5值
     * @param chunk       第几片
     * @param moduleName  上传文件所在模块名称
     * @param fileTypeKey 文件类别
     * @return
     */
    @PostMapping("/uploadChunk")
    public String uploadChunk(MultipartFile file, String md5Value, Integer chunk, String moduleName, String fileTypeKey) {
        System.out.println("------------开始上传：" + file.getOriginalFilename());
        Boolean bool = uploaderService.uploadChunk(file, md5Value, chunk, moduleName, fileTypeKey);
        if (!bool) {
            return "FAILRE";
        }
        return "SUCCESS";
    }


    /**
     * 合成分片
     *
     * @param chunkCount  总片数
     * @param md5Value    文件MD5值
     * @param fileName    文件名称
     * @param moduleName  上传文件所在模块名称
     * @param fileTypeKey 文件类别
     * @return
     */
    @PostMapping("/merge")
    public String merge(Integer chunkCount, String md5Value, String fileName, String moduleName, String fileTypeKey) {
        String fileId = uploaderService.merge(chunkCount, md5Value, fileName, moduleName, fileTypeKey);
        if (StringUtils.isBlank(fileId)) {
            return "FAILRE";
        }
        return "SUCCESS";
    }

}
