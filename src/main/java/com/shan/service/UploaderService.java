package com.shan.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {


    Boolean checkChunk(String md5Value, Integer chunk, String moduleName, String fileTypeKey);

    Boolean uploadChunk(MultipartFile file, String md5Value, Integer chunk, String moduleName, String fileTypeKey);

    String merge(Integer chunkCount, String md5Value, String fileName, String moduleName, String fileTypeKey);

    String specifiedDirectory(String fileTypeKey);

}
