package com.app.sinkinchan.smartstock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.utils.ResourceManager;

import org.jsoup.helper.StringUtil;

/**
 * 等待框
 */
public class ProgressDialog extends Dialog {
    TextView tvInfo;
    ImageView loading_icon;
    /**
     * 标志
     */
    String info = null;

    public ProgressDialog(Context context, String info) {
        super(context, R.style.myDialog);
        show();
        Window window = getWindow();
        window.setContentView(R.layout.dialog_progress);
        tvInfo = (TextView) window.findViewById(R.id.tvInfo);
        loading_icon = (ImageView) window.findViewById(R.id.loading_icon);
        runCircle();
        if (StringUtil.isBlank(info)) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setText(info);
        }
        this.info = info;
    }

    Animation operatingAnimForwardRotation;

    public void runCircle() {
        operatingAnimForwardRotation = AnimationUtils.loadAnimation(ResourceManager.getContext(), R.anim.loading_rotatation);
        LinearInterpolator lin1 = new LinearInterpolator();
        operatingAnimForwardRotation.setInterpolator(lin1);
        loading_icon.startAnimation(operatingAnimForwardRotation);
    }

    /**
     * 设置dialog显示的内容
     */
    public void setMessage(String info) {
        if (StringUtil.isBlank(info)) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setText(info);
        }

    }

    public String getMessage() {
        return info;
    }
}
