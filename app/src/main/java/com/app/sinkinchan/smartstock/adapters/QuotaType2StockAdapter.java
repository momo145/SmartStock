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
public class QuotaType2StockAdapter extends RecyclerView.Adapter<QuotaType2StockAdapter.ViewHolder> implements BaseAdapter<QuotaStockPage.QuotaStockType2> {
    //    private QuotaStockPage quotaStockPage;
    private QuotaStockPage.QuotaStockType2 quotaStockType2;
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
        return SourceManager.StockType.QuotaStockType2;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public QuotaStockPage.QuotaStockType2 getPage() {
        return quotaStockType2;
    }

    public QuotaType2StockAdapter(Context context, QuotaStockPage.QuotaStockType2 quotaStockType2) {
        this.quotaStockType2 = quotaStockType2;
        this.context = context;
    }

    public QuotaStockPage.QuotaStockType2 getQuotaStockType2() {
        return quotaStockType2;
    }

    public QuotaType2StockAdapter setQuotaStockType2(QuotaStockPage.QuotaStockType2 quotaStockType2) {
        this.quotaStockType2 = quotaStockType2;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.quota_type2_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        QuotaType2StockAdapter.ViewHolder holder = new QuotaType2StockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        QuotaStockPage.QuotaStockType2.DataBean dataBean = quotaStockType2.getData().get(position);
        viewHolder.getBinding().setVariable(BR.quotaType2, dataBean);
        viewHolder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return quotaStockType2.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public QuotaType2StockAdapter.ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
