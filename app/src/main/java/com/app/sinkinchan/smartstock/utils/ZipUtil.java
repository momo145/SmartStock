package com.app.sinkinchan.smartstock.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-16 下午10:27
 **/
public class ZipUtil {
    /**
     * 压缩
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压缩
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
            out.flush();
        }
        gunzip.close();
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toString("UTF-8");
    }

    // // 测试方法
    // public void main(String[] args) throws IOException {
    // String temp = "中文。，、。2145@！#¥¥%%⋯⋯⋯⋯⋯⋯*&“”《》？：“}{+——）（）*（&*⋯⋯%¥#@！|";
    // System.out.println("原字符串=" + temp);
    // System.out.println("原长=" + temp.length());
    // String temp1 = ZipUtil.compress(temp);
    // System.out.println("压缩后的字符串=" + temp1);
    // System.out.println("压缩后的长=" + temp1.length());
    // System.out.println("解压后的字符串=" + ZipUtil.uncompress(temp1));
    // }
}
