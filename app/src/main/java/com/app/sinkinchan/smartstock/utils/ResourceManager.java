package com.app.sinkinchan.smartstock.utils;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.app.sinkinchan.smartstock.app.App;

import java.util.List;

/**
 * 系统资源管理器
 *
 * @author : miaozh
 * @since : 2015/10/22  10:53
 */
public class ResourceManager {

    public static Context getContext() {
        return App.getInstance();
    }

    /**
     * 获取appname
     *
     * @return
     */
    public static String getAppName() {
        return getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString();
    }

    private final static App app = App.getInstance();

    /**
     * 获取文本
     *
     * @param StringId
     * @return
     */
    public static String getString(final int StringId) {
        return app.getString(StringId);
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Resources getResources() {
        return app.getResources();
    }

    /**
     * 获得状态栏高度
     */
    public static int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }


    public static View statusBarView = null;

    /**
     * 移除TapStatusBarToTop事件
     */
    public static void removeTapStatusBarToTop() {
        final WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        if (statusBarView != null) {
            windowManager.removeView(statusBarView);
            statusBarView = null;
        }
    }

    static GestureDetector gestureDetector = null;

    public static GestureDetector getGestureDetector(final ListView listView) {
        gestureDetector = new GestureDetector(App.getInstance(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                LogUtil.d("" + velocityX);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                listView.setSelection(0);
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                listView.setSelection(0);
                return super.onDoubleTapEvent(e);
            }
        });
        return gestureDetector;
    }

    /**
     * 判断手机设备是否有陀螺仪
     *
     * @return
     */
    public static boolean hasGyroscope() {
        return hasSensor(Sensor.TYPE_GYROSCOPE);
    }

    /**
     * 判断是否有某传感器¬
     *
     * @param key
     * @return
     */
    private static boolean hasSensor(int key) {
        boolean flag = false;
        //从系统服务中获得传感器管理器
        SensorManager sm = (SensorManager) App.getInstance().getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(key);
        if (allSensors != null && allSensors.size() > 0) {
            flag = true;
        }
        return flag;
    }
}
