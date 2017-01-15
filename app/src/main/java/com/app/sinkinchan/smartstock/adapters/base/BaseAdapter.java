package com.app.sinkinchan.smartstock.adapters.base;

import com.sinkinchan.stock.sdk.SourceManager;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-05 下午8:50
 **/
public interface BaseAdapter<T> {
    boolean isLoading();

    void setLoading(boolean loading);

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    T getPage();

    SourceManager.StockType getType();

}
