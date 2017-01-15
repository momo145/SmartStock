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
public class QuotaType4StockAdapter extends RecyclerView.Adapter<QuotaType4StockAdapter.ViewHolder> implements BaseAdapter<QuotaStockPage.QuotaStockType4> {
    //    private QuotaStockPage quotaStockPage;
    private QuotaStockPage.QuotaStockType4 quotaStockType4;
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
        return SourceManager.StockType.QuotaStockType4;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public QuotaStockPage.QuotaStockType4 getPage() {
        return quotaStockType4;
    }

    public QuotaType4StockAdapter(Context context, QuotaStockPage.QuotaStockType4 quotaStockType4) {
        this.quotaStockType4 = quotaStockType4;
        this.context = context;
    }

    public QuotaStockPage.QuotaStockType4 getQuotaStockType4() {
        return quotaStockType4;
    }

    public QuotaType4StockAdapter setQuotaStockType4(QuotaStockPage.QuotaStockType4 quotaStockType4) {
        this.quotaStockType4 = quotaStockType4;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.quota_type4_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        QuotaType4StockAdapter.ViewHolder holder = new QuotaType4StockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        QuotaStockPage.QuotaStockType4.DataBean dataBean = quotaStockType4.getData().get(position);
        viewHolder.getBinding().setVariable(BR.quotaType4, dataBean);
        viewHolder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return quotaStockType4.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public QuotaType4StockAdapter.ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
