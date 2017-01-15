package com.app.sinkinchan.smartstock.constant;

import android.os.Environment;

import java.io.File;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 下午5:17
 **/
public class Constants {
    public static File APP_FILE_CACHE_DIR = new File(Environment.getExternalStorageDirectory(), "SmartStock");
    public static File APP_FILE_PDF_CACHE_DIR = new File(APP_FILE_CACHE_DIR.getAbsolutePath(), "PDF");
    public static File APP_UPDATE_URL = new File(APP_FILE_CACHE_DIR.getAbsolutePath(), "SmartStock.apk");

    public static void createDir() {
        if (!APP_FILE_PDF_CACHE_DIR.exists()) {
            APP_FILE_PDF_CACHE_DIR.mkdirs();
        }
    }
}
