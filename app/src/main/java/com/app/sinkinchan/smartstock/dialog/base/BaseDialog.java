package com.app.sinkinchan.smartstock.dialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.app.sinkinchan.smartstock.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-07-24 上午9:47
 **/
public class BaseDialog extends Dialog implements DialogInterface.OnDismissListener, View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {
    protected Activity context;

    public static String TAG = null;


    public BaseDialog(Context context) {
        super(context);
        this.context = (Activity) context;
        init();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.context = (Activity) context;
        init();
    }


    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 实例化
     */
    protected void init() {
        TAG = this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    /**
     * 处理事件方法
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {

    }

    protected void LogDebug(String msg) {
        Log.d(TAG, "LogDebug: " + msg);
    }


    protected void LogError(String msg) {
        Log.d(TAG, "LogError: " + msg);
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
        LogError("register dialogEventBus");
    }

    protected void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        unRegisterEventBus();
        LogError("unregister dialogEventBus");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
