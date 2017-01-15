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
import com.sinkinchan.stock.sdk.bean.ResearchStockPage;


/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午5:24
 **/
public class ResearchStockAdapter extends RecyclerView.Adapter<ResearchStockAdapter.ViewHolder> implements BaseAdapter<ResearchStockPage> {
    private ResearchStockPage researchStockPage;
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
        return SourceManager.StockType.ResearchStock;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public ResearchStockPage getPage() {
        return researchStockPage;
    }

    public ResearchStockAdapter(Context context, ResearchStockPage researchStockPage) {
        this.researchStockPage = researchStockPage;
        this.context = context;
    }

    public ResearchStockPage getResearchStockPage() {
        return researchStockPage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card_view_research_stock_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        ResearchStockAdapter.ViewHolder holder = new ResearchStockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ResearchStockPage.DataBean dataBean = researchStockPage.getData().get(position);
        viewHolder.getBinding().setVariable(BR.researchStock, dataBean);
        viewHolder.getBinding().executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return researchStockPage.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public ResearchStockAdapter.ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
