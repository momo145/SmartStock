package com.app.sinkinchan.smartstock.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sinkinchan.smartstock.BR;
import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.adapters.base.BaseAdapter;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.ActiveStocksPage;


/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午5:24
 **/
public class ActiveStockAdapter extends RecyclerView.Adapter<ActiveStockAdapter.ViewHolder> implements BaseAdapter<ActiveStocksPage> {
    private ActiveStocksPage activeStocksPage;
    Context context;
    private boolean isLoading = false;
    private boolean isRefreshing = false;

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public SourceManager.StockType getType() {
        return SourceManager.StockType.ActiveStock;
    }

    @Override
    public ActiveStocksPage getPage() {
        return activeStocksPage;
    }

    public ActiveStockAdapter(Context context, ActiveStocksPage activeStocksPage) {
        this.activeStocksPage = activeStocksPage;
        this.context = context;
    }

    public ActiveStocksPage getActiveStocksPage() {
        return activeStocksPage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card_view_stock_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ActiveStocksPage.DataBean dataBean = activeStocksPage.getData().get(position);
        viewHolder.getBinding().setVariable(BR.activeStockData, dataBean);
        viewHolder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return activeStocksPage.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

    }


}
