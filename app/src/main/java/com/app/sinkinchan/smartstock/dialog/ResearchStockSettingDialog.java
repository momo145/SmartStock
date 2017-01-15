package com.app.sinkinchan.smartstock.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.fragments.ResearchStockFragment;
import com.sinkinchan.stock.sdk.bean.ResearchStockPage;

import org.greenrobot.eventbus.EventBus;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-15 下午5:20
 **/
public class ResearchStockSettingDialog {
    private static ResearchStockSettingDialog ourInstance = new ResearchStockSettingDialog();

    public static ResearchStockSettingDialog getInstance() {
        return ourInstance;
    }

    private ResearchStockSettingDialog() {
    }

    AlertDialog.Builder settingDialog = null;
    int chooseIndex = 1;
    ResearchStockPage.Type type = null;

    public void show(@NonNull final Activity activity, @NonNull final ResearchStockPage.Param param) {
        if (param == null) {
            return;
        }
        type = null;
        chooseIndex = param.getType().ordinal();
        settingDialog = new AlertDialog.Builder(activity);
        settingDialog.setTitle("研报股设置");
        settingDialog.setSingleChoiceItems(new String[]{ResearchStockPage.Type.today.getDesc(),
                ResearchStockPage.Type.five.getDesc(), ResearchStockPage.Type.thirty.getDesc()}, chooseIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseIndex = which;
            }
        });
        settingDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (chooseIndex) {
                    case 0:
                        type = ResearchStockPage.Type.today;
                        break;
                    case 1:
                        type = ResearchStockPage.Type.five;
                        break;
                    case 2:
                        type = ResearchStockPage.Type.thirty;
                        break;
                    default:
                        type = ResearchStockPage.Type.five;
                        break;
                }
                ResearchStockFragment.param.setType(type);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.REFRESH_STOCK));
//                ToastUtil.showLongToast(activity, "chooseIndex=" + type.getDesc());
            }
        });
        settingDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        settingDialog.show();
    }

}
