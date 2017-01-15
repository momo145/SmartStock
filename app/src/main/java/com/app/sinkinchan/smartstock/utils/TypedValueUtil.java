package com.app.sinkinchan.smartstock.utils;

import android.util.TypedValue;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 下午12:08
 **/
public class TypedValueUtil {
    public static float applyDimension(int unit, float value) {
        return TypedValue.applyDimension(unit, value, Base.getDisplayMetrics());
    }

    public static float complexToDimension(int data) {
        return TypedValue.complexToDimension(data, Base.getDisplayMetrics());
    }

    public static int complexToDimensionPixelOffset(int data) {
        return TypedValue.complexToDimensionPixelOffset(data, Base.getDisplayMetrics());
    }

    public static int complexToDimensionPixelSize(int data) {
        return TypedValue.complexToDimensionPixelSize(data, Base.getDisplayMetrics());
    }
}
