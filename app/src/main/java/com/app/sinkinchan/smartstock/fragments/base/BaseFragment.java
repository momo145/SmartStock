package com.app.sinkinchan.smartstock.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.mina.MinaService;
import com.app.sinkinchan.smartstock.utils.DeviceUtils;
import com.baidu.mobstat.StatService;
import com.sinkinchan.transport.module.ActionResponse;
import com.sinkinchan.transport.module.FunctionBean;
import com.sinkinchan.transport.module.TransportType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by sinkinchan on 2015/8/4.
 */
public class BaseFragment extends Fragment {
    public static String TAG = null;

    public BaseFragment() {
        TAG = this.getClass().getSimpleName();
    }

    public enum Type {
        ActiveStockFragment, DeathSquadStockFragment, ResearchStock, QuotaType1StockFragment,
        QuotaType2StockFragment, QuotaType3StockFragment, QuotaType4StockFragment
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
    }


    protected void LogDebug(String msg) {
        Log.d(TAG, "LogDebug: " + msg);
    }


    protected void LogError(String msg) {
        Log.d(TAG, "LogError: " + msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        switch (event.getType()) {
            case USE_FUNCTION:
                ActionResponse actionResponse = (ActionResponse) event.getArgs();
                showActionResponse(actionResponse);
                break;
        }
    }

    protected void showActionResponse(ActionResponse actionResponse) {
        if (actionResponse != null) {
            //询问活跃股是否扣积分
            /*if (StringUtils.equals(actionResponse.getName(), SourceManager.StockType.ActiveStock.title)) {


            }*/
            switch (actionResponse.getResponseType()) {
                case Ask:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.currentActivity);
                    dialog.setTitle("提醒")
                            .setMessage(actionResponse.getDesc())
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", (dialogInterface, witch) -> {
                                useFunction(actionResponse.getFunctionName());
                            })
                            .setCancelable(false)
                            .show();
                    break;
                case Yes:
                    //服务器添加数据成功
                    break;
            }
        }
    }

    @Subscribe
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    protected void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void OnClickEvent(View view) {
    }


    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public static final String SOURCE_DATE = "数据源日期:%s";

    protected void showMessage(String message) {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_MESSAGE, message));
    }

    /**
     * 询问是否使用积分
     *
     * @param type
     */
    protected void askUseFunction(String type) {
        String deviceId = DeviceUtils.getUniquePsuedoID();
        FunctionBean functionBean = new FunctionBean();
        functionBean.setId(deviceId).setFunctionName(type).setType(TransportType.askUseFunction);
        MinaService.getInstance().write(functionBean, null);
    }

    /**
     * 确认使用积分
     *
     * @param type
     */
    protected void useFunction(String type) {
        String deviceId = DeviceUtils.getUniquePsuedoID();
        FunctionBean functionBean = new FunctionBean();
        functionBean.setId(deviceId).setFunctionName(type).setType(TransportType.useFunction);
        MinaService.getInstance().write(functionBean, null);
    }
}
