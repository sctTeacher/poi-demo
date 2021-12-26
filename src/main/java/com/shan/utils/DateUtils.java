//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static DateUtils instance;

    public DateUtils() {
    }

    public static DateUtils getInstance() {
        if (instance == null) {
            instance = new DateUtils();
        }

        return instance;
    }

    public long dateformat(String datestr) throws Exception {
        return this.dateformat(datestr, "yyyy-MM-dd HH:mm:ss").getTime();
    }

    public Date dateformat(String datestr, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(datestr);
    }

    public String dateFormat() {
        return this.dateFormat(System.currentTimeMillis());
    }

    public String dateFormat(Date date) {
        return this.dateFormat(date, "yyyyMMddHHmmssSSS");
    }

    public String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public String dateFormat(Long currentTimeMillis) {
        return this.dateFormat(new Date(currentTimeMillis), "yyyy/MM/dd");
    }

    public String dateformat2(String datestr) throws Exception {
        return String.valueOf(this.dateformat(datestr));
    }

    public String dateFormat2(Date date) {
        return this.dateFormat(date, "yyyyMMddHHmmss");
    }

    public String dateFormat3(Date date) {
        return this.dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public String dateFormat4(Date date) {
        return this.dateFormat(date, "yyyy-MM-dd HH:mm:ss:SSS");
    }

    public String dateFormat5(Date date) {
        return this.dateFormat(date, "yyyy");
    }

    public String dateFormat6() {
        return this.dateFormat(new Date(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss");
    }

    public String dateFormat6(Date date) {
        return this.dateFormat(date, "yyyy/MM/dd HH:mm:ss");
    }

    public String dateFormat7(Date date) {
        return this.dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public long dateformat8(String datestr) throws Exception {
        return this.dateformat(datestr, "yyyy/MM/dd HH:mm:ss").getTime();
    }

    public long dateformat9(String datestr) throws Exception {
        return this.dateformat(datestr, "yyyyMMddHHmmss").getTime();
    }

    public String dateFormat10(Long currentTimeMillis) {
        return this.dateFormat(new Date(currentTimeMillis), "yyyy-MM-dd");
    }
}
