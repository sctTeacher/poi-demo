//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shan.file;

import java.io.InputStream;
import java.util.List;

import com.shan.entity.bean.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeUploader {
    Boolean checkChunk(String var1, String var2, Integer var3);

    Boolean compress(List<String> var1, String var2);

    Boolean copy(String var1, String var2);

    Boolean delete(String var1);

    InputStream download(String var1);

    Boolean exists(String var1);

    Long getDiskSize(String var1);

    List<FileInfo> listFiles(String var1, boolean var2);

    Boolean merge(String var1, String var2, String var3, Integer var4, String var5);

    Boolean mkDir(String var1);

    Boolean move(String var1, String var2);

    Boolean rename(String var1, String var2);

    Boolean upload(MultipartFile var1, String var2, String var3, Integer var4);
}
