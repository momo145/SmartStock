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
import com.sinkinchan.stock.sdk.bean.DeathSquadStockPage;


/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午5:24
 **/
public class DeathSquadStockAdapter extends RecyclerView.Adapter<DeathSquadStockAdapter.ViewHolder> implements BaseAdapter<DeathSquadStockPage> {
    private DeathSquadStockPage deathSquadStockPage;
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
        return SourceManager.StockType.DeathSquadStock;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.isRefreshing = refreshing;
    }

    @Override
    public DeathSquadStockPage getPage() {
        return deathSquadStockPage;
    }

    public DeathSquadStockAdapter(Context context, DeathSquadStockPage deathSquadStockPage) {
        this.deathSquadStockPage = deathSquadStockPage;
        this.context = context;
    }

    public DeathSquadStockPage getDeathSquadStockPage() {
        return deathSquadStockPage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.death_squad_stock_item, parent, false);
        binding.setVariable(BR.stockUtil, StockUtil.getInstance());
        DeathSquadStockAdapter.ViewHolder holder = new DeathSquadStockAdapter.ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }


    String msg = "";

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DeathSquadStockPage.DataBean dataBean = deathSquadStockPage.getData().get(position);
        viewHolder.getBinding().setVariable(BR.deathStock, dataBean);
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return deathSquadStockPage.getData().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public DeathSquadStockAdapter.ViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
