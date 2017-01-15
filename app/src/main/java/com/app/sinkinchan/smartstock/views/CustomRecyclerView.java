package com.app.sinkinchan.smartstock.views;


import android.content.Context;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午10:20
 **/
public class CustomRecyclerView extends XRecyclerView {

    public boolean isLoading;
    public boolean isRefreshing;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void refreshComplete() {
        super.refreshComplete();
        isRefreshing = false;
    }

    @Override
    public void loadMoreComplete() {
        super.loadMoreComplete();
        isLoading = false;
    }


}

