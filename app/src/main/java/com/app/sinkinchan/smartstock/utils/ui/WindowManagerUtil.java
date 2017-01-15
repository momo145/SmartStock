package com.app.sinkinchan.smartstock.utils.ui;

import android.view.Display;
import android.view.View;

import com.app.sinkinchan.smartstock.utils.ServiceUtil;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 上午11:53
 **/
public class WindowManagerUtil {
    public static Display getDefaultDisplay() {
        return ServiceUtil.getWindowManager().getDefaultDisplay();
    }

    public static void removeViewImmediate(View view) {
        ServiceUtil.getWindowManager().removeViewImmediate(view);
    }
}
