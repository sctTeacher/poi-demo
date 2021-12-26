package com.shan.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static final char[] hexCode = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String calcMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString().toLowerCase();
    }

    public static String calcMd5(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexCode[byte0 >>> 4 & 0xf];
                str[k++] = hexCode[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Fast MD5 PLUS
     */
    public static String fastMD5(File file) {
        int len = 10000;
        try {
            if(file.length() < 10000000){
                return com.twmacinta.util.MD5.asHex(com.twmacinta.util.MD5.getHash(file));
            }
            byte pond[] =new byte [len];
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(file.length() - len);
            raf.read(pond, 0, len);
            return calcMd5(toHexString(pond));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\软件\\360极速浏览器.zip");
        String s = Md5Util.fastMD5(file);
        System.out.println(s);
    }

}

