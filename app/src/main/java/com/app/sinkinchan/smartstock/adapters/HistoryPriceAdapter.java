package com.app.sinkinchan.smartstock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.sinkinchan.stock.sdk.bean.StockHistoryPrice;
import com.sinkinchan.stock.sdk.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-07 下午11:40
 **/
public class HistoryPriceAdapter extends RecyclerView.Adapter<HistoryPriceAdapter.ViewHolder> {

    private Context context;
    private List<StockHistoryPrice> stockHistoryPrices;

    public HistoryPriceAdapter(Context context, List<StockHistoryPrice> stockHistoryPrices) {
        this.context = context;
        this.stockHistoryPrices = stockHistoryPrices;
    }

    @Override
    public HistoryPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(context)
                .inflate(R.layout.card_view_stock_history_price_item, parent, false);
        return new HistoryPriceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryPriceAdapter.ViewHolder viewHolder, int position) {
        StockHistoryPrice historyPrice = stockHistoryPrices.get(position);
        Date time = historyPrice.getTime();
        if (time != null) {
            viewHolder.tv_time.setText(DateUtil.toString(historyPrice.getTime()));
        }

//                        viewHolder.ActiveStocks_tv_message.setText(App.getInstance()
//                                .getString(R.string.active_stock_message_text, change, percent, convert(dataBean.getHs_lb(), 100) + ""));
        double upDownPercent = historyPrice.getUpDownPercent();
        if (upDownPercent < 0) {
            viewHolder.tv_updown.setTextColor(Color.GREEN);
            viewHolder.tv_price.setTextColor(Color.GREEN);
        } else {

            viewHolder.tv_updown.setTextColor(ResourceManager.getResources().getColor(R.color.md_red_600));
            viewHolder.tv_price.setTextColor(ResourceManager.getResources().getColor(R.color.md_red_600));
        }
        viewHolder.tv_price.setText(historyPrice.getClosePrice() + "");
        viewHolder.tv_height.setText("高 " + historyPrice.getHeightPrice());
        viewHolder.tv_updown.setText(historyPrice.getUpDownPrice() + " " + historyPrice.getUpDownPercent() + "%");
        viewHolder.tv_low.setText("低 " + historyPrice.getLowPrice());
        viewHolder.tv_open.setText("开 " + historyPrice.getOpenPrice());
        viewHolder.tv_change.setText("换 " + historyPrice.getChange() + "%");
        viewHolder.tv_volume.setText("量 " + historyPrice.getVolumeOfBusiness() + "手");
        viewHolder.tv_turnover.setText("额 " + historyPrice.getTurnover() + "万");
    }

    @Override
    public int getItemCount() {
        return stockHistoryPrices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time;
        public TextView tv_price;
        public TextView tv_updown;
        public TextView tv_height;
        public TextView tv_low;
        public TextView tv_open;
        public TextView tv_change;
        public TextView tv_volume;
        public TextView tv_turnover;

        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_updown = (TextView) view.findViewById(R.id.tv_updown);
            tv_height = (TextView) view.findViewById(R.id.tv_height);
            tv_low = (TextView) view.findViewById(R.id.tv_low);
            tv_open = (TextView) view.findViewById(R.id.tv_open);
            tv_change = (TextView) view.findViewById(R.id.tv_change);
            tv_volume = (TextView) view.findViewById(R.id.tv_volume);
            tv_turnover = (TextView) view.findViewById(R.id.tv_turnover);
        }
    }
}
