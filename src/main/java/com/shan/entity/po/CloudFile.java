package com.shan.entity.po;

import lombok.Data;

import java.util.Date;


@Data
public class CloudFile {

    private String id;

    // 文件名称 （列如123.txt,该字段存  123）
    private String name;

    // 文件类型 （列如123.txt,该字段存  .txt）
    private String type;

    // 文件绝对路径
    private String path;

    // 文件MD5值
    private String md5;

    // 文件大小 单位：字节
    private Long fileSize;

    // 创建时间
    private Date gmtCreate;
}
