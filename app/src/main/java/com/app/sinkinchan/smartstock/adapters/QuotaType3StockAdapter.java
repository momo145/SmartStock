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
import com.sinkinchan.stock.sdk.bean.QuotaStockPage;


/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午5:24
 **/
public class QuotaType3StockAdapter extends RecyclerView.Adapter<QuotaType3StockAdapter.ViewHolder> implements BaseAdapter<QuotaStockPage.QuotaStockType3> {
    //    private QuotaStockPage quotaStockPage;
    private QuotaStockPage.QuotaStockType3 quotaStockType3;
    Context context;
    public boolean isLoading;
    public boolean isRefreshing;

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
    public SourceManager.StockType getType() {
        return SourceManager.StockType.QuotaStockType3;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public QuotaStockPage.QuotaStockType3 getPage() {
        return quotaStockType3;
    }

    public QuotaType3StockAdapter(Context context, QuotaStockPage.QuotaStockType3 quotaStockType3) {
        this.quotaStockType3 = quotaStockType3;
        this.context = context;
    }

    public QuotaStockPage.QuotaStockType3 getQuotaStockType3() {
        return quotaStockType3;
    }

    public QuotaType3StockAdapter setQuotaStockType3(QuotaStockPage.QuotaStockType3 quotaStockType3) {
        this.quotaStockType3 = quotaStockType3;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.quota_type3_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        QuotaType3StockAdapter.ViewHolder holder = new QuotaType3StockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        QuotaStockPage.QuotaStockType3.DataBean dataBean = quotaStockType3.getData().get(position);
        viewHolder.getBinding().setVariable(BR.quotaType3, dataBean);
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return quotaStockType3.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public QuotaType3StockAdapter.ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
