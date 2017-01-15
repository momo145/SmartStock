package com.app.sinkinchan.smartstock.activitys.base;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.dialog.ProgressDialog;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.app.sinkinchan.smartstock.utils.ToastUtil;
import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-07-16 下午11:37
 **/
public abstract class BaseActivity extends AppCompatActivity {
    public static String TAG = null;
    public static Activity currentActivity;
    public final static String STOCK_CODE_KEY = "stock_code_key";
    public final static String STOCK_NAME_KEY = "stock_name_key";
    public final static String WEB_VIEW_LOAD_URL = "web_view_load_url";
    public final static String WEB_VIEW_TITLE = "web_view_title";
    public final static String DATA_BEAN_KEY = "data_bean_key";
    public final static String STOCK_HANDICAP_MORE_TITLE = "F10";
    protected static Bitmap activityScreenBitmap;

    public BaseActivity() {
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //初始化
    protected abstract void init();

    //注册事件
    protected abstract void registerListener();

    //反注册事件
    protected abstract void unRegisterListener();

    //注册事件
    protected void createListener() {
    }

    //反注册事件
    protected void destroyListener() {
    }

    //EventBus 处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case REWARD_POINTS:
                int reward = -1;
                if (event.getArgs() != null) {
                    reward = (int) event.getArgs();
                }
                if (reward != -1) {
                    ToastUtil.showLongToast(getApplicationContext(),
                            String.format(ResourceManager.getString(R.string.reward_points_text), String.valueOf(reward)));
                }
                break;
            case SHOW_POINTS_POOL_DIALOG:
                showPointsPoolDialog();
                break;
        }
    }

    protected View getActivityView() {
        return getWindow().getDecorView();
    }

    private void registerEventBus() {
        LogDebug("registerEventBus");
        EventBus.getDefault().register(this);
    }

    private void unRegisterEventBus() {
        LogDebug("unRegisterEventBus");
        EventBus.getDefault().unregister(this);
    }


    protected void LogDebug(String msg) {
        Log.d(TAG, "LogDebug: " + msg);
    }


    protected void LogError(String msg) {
        Log.d(TAG, "LogError: " + msg);
    }


   /* protected void showCommonDialog(DialogInfo dialogInfo) {

    }*/

    /*BaseDialog CommonDialog = null;

    protected void dismissCommonDialog() {
        if (CommonDialog != null && CommonDialog.isShowing()) {
            CommonDialog.dismiss();
        }
    }*/

    protected void hideActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().hide();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        createListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
        destroyListener();
    }

    private ProgressDialog progressDialog = null;

    /**
     * 显示读取pid的dialog
     */
    protected void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(this, msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void onClick(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        unRegisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        currentActivity = this;
        registerListener();
    }

    protected void setBackMenu() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 显示积分不足dialog
     */
    protected void showPointsPoolDialog() {
        /*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("你的积分已不足")
                .setMessage("需要50积分才能继续使用该功能.\n已禁用刷新,加载更多等功能.")
                .setNegativeButton("取消", null)
                .setPositiveButton("去赚取积分", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OffersManager.getInstance(currentActivity).showOffersWall();
                    }
                })
                .setCancelable(false)
                .show();*/
    }

}
