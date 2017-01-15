package com.app.sinkinchan.smartstock.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.app.App;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.fragments.ActiveStockFragment;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.app.sinkinchan.smartstock.views.SlideSelectView;
import com.sinkinchan.stock.sdk.bean.ActiveStocksPage;

import org.greenrobot.eventbus.EventBus;

import static com.app.sinkinchan.smartstock.utils.ResourceManager.getString;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.CHANGE_10;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.CHANGE_15;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.CHANGE_5;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.CHANGE_7;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.LB_1;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.LB_3;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.LB_5;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.LB_7;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.UP_10;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.UP_3;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.UP_6;
import static com.sinkinchan.stock.sdk.bean.ActiveStocksPage.Param.Type.UP_8;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-06 下午9:34
 **/
public class ActiveStockSettingDialog {
    private static ActiveStockSettingDialog ourInstance = new ActiveStockSettingDialog();

    public static ActiveStockSettingDialog getInstance() {
        return ourInstance;
    }

    private ActiveStockSettingDialog() {
    }


    AlertDialog.Builder activeStockDialog = null;
    ActiveStocksPage.Param paramTemp;

    public void show(@NonNull Activity activity, @NonNull final ActiveStocksPage.Param param) {
        if (param == null) {
            return;
        }
        paramTemp = new ActiveStocksPage.Param(param.getChange(), param.getLb(), param.getUp(), param.getPage(), param.getPageSize());
        activeStockDialog = new AlertDialog.Builder(activity);
        activeStockDialog.setTitle("设置活跃股参数");
        activeStockDialog.setCancelable(false);
        activeStockDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActiveStockFragment.param = paramTemp;
            }
        });
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_active_stock_setting, null);
        activeStockDialog.setView(v);
        initSlideSelectView(v, param);
        activeStockDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.REFRESH_STOCK));
            }
        });
        activeStockDialog.show();
        //默认值
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    App.getInstance().runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            change_slideSelectView.setCurrentPosition(param.getChange().index);
                            up_slideSelectView.setCurrentPosition(param.getUp().index);
                            lb_slideSelectView.setCurrentPosition(param.getLb().index);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    SlideSelectView change_slideSelectView;
    TextView tvCurrentValue;
    //涨幅
    SlideSelectView up_slideSelectView;
    TextView tvUpCurrentValue;
    //量比
    SlideSelectView lb_slideSelectView;
    TextView tvLBCurrentValue;
    ActiveStocksPage.Param.Type change = CHANGE_5;
    ActiveStocksPage.Param.Type up = UP_3;
    ActiveStocksPage.Param.Type lb = LB_1;

    private void initSlideSelectView(@NonNull View view, @NonNull final ActiveStocksPage.Param param) {
//        换手率
        change_slideSelectView = (SlideSelectView) view.findViewById(R.id.change_slideSelectView);
        tvCurrentValue = (TextView) view.findViewById(R.id.tvCurrentValue);
        String[] textStrings = ResourceManager.getResources().getStringArray(R.array.change_string);
        change_slideSelectView.setString(textStrings);
        change_slideSelectView.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                String str = getString(R.string.setting_tv_change_text);
                if (index == CHANGE_5.index) {
                    str = String.format(str, CHANGE_5.text);
                    change = CHANGE_5;
                } else if (index == CHANGE_7.index) {
                    change = CHANGE_7;
                    str = String.format(str, CHANGE_7.text);
                } else if (index == CHANGE_10.index) {
                    change = CHANGE_10;
                    str = String.format(str, CHANGE_10.text);
                } else {
                    change = CHANGE_15;
                    str = String.format(str, CHANGE_15.text);
                }
                param.setChange(change);
                tvCurrentValue.setText(str);
            }
        });

//        涨幅
        up_slideSelectView = (SlideSelectView) view.findViewById(R.id.up_slideSelectView);
        tvUpCurrentValue = (TextView) view.findViewById(R.id.tvUpCurrentValue);
        textStrings = ResourceManager.getResources().getStringArray(R.array.up_string);
        up_slideSelectView.setString(textStrings);
        up_slideSelectView.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                String str = getString(R.string.setting_tv_up_text);
                if (index == UP_10.index) {
                    up = UP_10;
                    str = String.format(str, UP_10.text);
                } else if (index == UP_8.index) {
                    up = UP_8;
                    str = String.format(str, UP_8.text);
                } else if (index == UP_6.index) {
                    up = UP_6;
                    str = String.format(str, UP_6.text);
                } else {
                    up = UP_3;
                    str = String.format(str, UP_3.text);
                }
                param.setUp(up);
                tvUpCurrentValue.setText(str);
            }
        });
        //量比
        lb_slideSelectView = (SlideSelectView) view.findViewById(R.id.lb_slideSelectView);
        tvLBCurrentValue = (TextView) view.findViewById(R.id.tvLBCurrentValue);
        textStrings = ResourceManager.getResources().getStringArray(R.array.lb_string);
        lb_slideSelectView.setString(textStrings);
        lb_slideSelectView.setOnSelectListener(new SlideSelectView.onSelectListener() {
            @Override
            public void onSelect(int index) {
                String str = getString(R.string.setting_tv_lb_text);
                if (index == LB_7.index) {
                    lb = LB_7;
                    str = String.format(str, LB_7.text);
                } else if (index == LB_5.index) {
                    lb = LB_5;
                    str = String.format(str, LB_5.text);
                } else if (index == LB_3.index) {
                    lb = LB_3;
                    str = String.format(str, LB_3.text);
                } else {
                    lb = LB_1;
                    str = String.format(str, LB_1.text);
                }
                param.setLb(lb);
                tvLBCurrentValue.setText(str);
            }
        });


    }
}
