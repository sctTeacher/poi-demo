package com.shan;

import cn.hutool.core.io.unit.DataSizeUtil;

/**
 *  *     BYTES      1B      2^0     1
 *  *     KILOBYTES  1KB     2^10    1,024
 *  *     MEGABYTES  1MB     2^20    1,048,576
 *  *     GIGABYTES  1GB     2^30    1,073,741,824
 *  *     TERABYTES  1TB     2^40    1,099,511,627,776
 */
public class FileSizeTest {
    public static void main(String[] args) {
        Long b=1L;
        Long kb=1024L;
        Long mb=1048576L;
        Long gb=1073741842L;
        Long tb=1099511627776L;
        System.out.println("1b:"+DataSizeUtil.format( b));
        System.out.println("1kb:"+DataSizeUtil.format( kb));
        System.out.println("1mb:"+DataSizeUtil.format( mb));
        System.out.println("1gb:"+DataSizeUtil.format( gb));
        System.out.println("1tb:"+DataSizeUtil.format( tb));
    }





}
