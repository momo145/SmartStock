package com.app.sinkinchan.smartstock.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.app.sinkinchan.smartstock.R;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-06 下午9:34
 **/
public class AdDialog {

    AlertDialog.Builder adDialog = null;

    public void show(@NonNull Activity activity) {

        adDialog = new AlertDialog.Builder(activity);
        adDialog.setTitle("支持作者开发");
        adDialog.setCancelable(false);
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_ad, null);


        // 获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout) v.findViewById(R.id.ll_banner);

        // 将广告条加入到布局中
//        bannerLayout.addView(getBannerView());

        adDialog.setView(v);
        adDialog.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        adDialog.show();

    }

    /*private View getBannerView() {
        // 获取广告条
        View bannerView = BannerManager.getInstance(BaseActivity.currentActivity)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                        LogUtil.d("获取广告条成功.");
                    }

                    @Override
                    public void onSwitchBanner() {
                        LogUtil.d("onSwitchBanner.");
                    }

                    @Override
                    public void onRequestFailed() {
                        LogUtil.d("获取广告条失败.");
                    }
                });
        return bannerView;
    }*/

}
