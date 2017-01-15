package com.app.sinkinchan.smartstock.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 下午6:37
 **/
public class LoadingUtil {
    private Activity activity;
    private View view;

    public static LoadingUtil newInstance(View view) {
        return new LoadingUtil(view);
    }

    public static LoadingUtil newInstance(Activity activity) {
        return new LoadingUtil(activity);
    }

    private LoadingUtil(Activity activity) {
        this.activity = activity;
        initLoading(activity);
    }

    private LoadingUtil(View view) {
        this.view = view;
        initLoading(view);
    }


    Shimmer shimmer = null;
    ShimmerTextView shimmer_tv;

    protected void initLoading(View view) {
        shimmer = new Shimmer();
        shimmer_tv = (ShimmerTextView) view.findViewById(R.id.shimmer_tv);
        tv_reload = (TextView) view.findViewById(R.id.btn_reload);
        tv_none = (TextView) view.findViewById(R.id.tv_none);
        startShimmer();
    }

    protected void initLoading(Activity activity) {
        shimmer = new Shimmer();
        shimmer_tv = (ShimmerTextView) activity.findViewById(R.id.shimmer_tv);
        tv_reload = (TextView) activity.findViewById(R.id.btn_reload);
        tv_none = (TextView) activity.findViewById(R.id.tv_none);
        startShimmer();
    }

    private void startShimmer() {
        if (shimmer != null && shimmer_tv != null) {
            shimmer.start(shimmer_tv);
        }
    }

    public void showLoading() {
        hide();
        if (shimmer_tv != null && shimmer != null) {
            shimmer_tv.setVisibility(View.VISIBLE);
            shimmer.start(shimmer_tv);
        }
    }

    public void hideLoading() {
        if (shimmer != null && shimmer_tv != null) {
            if (shimmer_tv.getVisibility() == View.VISIBLE) {
                shimmer.cancel();
                shimmer_tv.setVisibility(View.GONE);
            }
        }
    }

    TextView tv_reload = null;

    public void showReload(View.OnClickListener clickListener) {
        hide();
        if (tv_reload != null) {
            tv_reload.setVisibility(View.VISIBLE);
            if (clickListener != null) {
                tv_reload.setOnClickListener(clickListener);
            }
        }
    }

    TextView tv_none = null;

    public void showNone() {
        hide();
        if (tv_none != null) {
            tv_none.setVisibility(View.VISIBLE);
        }
    }

    public void hideNone() {
        if (tv_none != null) {
            tv_none.setVisibility(View.GONE);
        }
    }

    public void hide() {
        hideLoading();
        hideReload();
        hideNone();
    }

    public void hideReload() {
        if (tv_reload != null && tv_reload.getVisibility() == View.VISIBLE) {
            tv_reload.setVisibility(View.GONE);

        }
    }

}
