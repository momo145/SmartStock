package com.app.sinkinchan.smartstock.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;
    private static View view;

    private ToastUtil() {
    }

    @SuppressLint("ShowToast")
    private static void getToast(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        if (view == null) {
            view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        }
        toast.setView(view);
    }

    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, CharSequence msg,
                                  int duration) {
        try {
            getToast(context);
            toast.setText(msg);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showToast(Context context, int resId, int duration) {
        try {
            // Looper.prepare();
            if (resId == 0) {
                return;
            }
            getToast(context);
            toast.setText(resId);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.show();
            // Looper.loop();
        } catch (Exception e) {
        }
    }


    public static void showToastThread(Context context, String message) {
        Looper.prepare();
        ToastUtil.showLongToast(context, message);
        Looper.loop();

    }
}
