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
public class QuotaType1StockAdapter extends RecyclerView.Adapter<QuotaType1StockAdapter.ViewHolder> implements BaseAdapter<QuotaStockPage.QuotaStockType1> {
    //    private QuotaStockPage quotaStockPage;
    private QuotaStockPage.QuotaStockType1 quotaStockType1;
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
        return SourceManager.StockType.QuotaStockType1;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public QuotaStockPage.QuotaStockType1 getPage() {
        return quotaStockType1;
    }

    public QuotaType1StockAdapter(Context context, QuotaStockPage.QuotaStockType1 quotaStockType1) {
        this.quotaStockType1 = quotaStockType1;
        this.context = context;
    }

    public QuotaStockPage.QuotaStockType1 getQuotaStockType1() {
        return quotaStockType1;
    }

    public QuotaType1StockAdapter setQuotaStockType1(QuotaStockPage.QuotaStockType1 quotaStockType1) {
        this.quotaStockType1 = quotaStockType1;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.quota_type1_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        QuotaType1StockAdapter.ViewHolder holder = new QuotaType1StockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        QuotaStockPage.QuotaStockType1.DataBean dataBean = quotaStockType1.getData().get(position);
        viewHolder.getBinding().setVariable(BR.quotaType1, dataBean);
        viewHolder.getBinding().executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return quotaStockType1.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
