package com.app.sinkinchan.smartstock.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.PDFViewerActivity;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.sinkinchan.stock.sdk.bean.StockNotice;
import com.sinkinchan.stock.sdk.utils.DateUtil;

import org.jsoup.helper.StringUtil;

import java.util.Date;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-07 下午11:40
 **/
public class StockNoticesAdapter extends RecyclerView.Adapter<StockNoticesAdapter.ViewHolder> {

    private Context context;
    private StockNotice stockNotice;

    public StockNoticesAdapter(Context context, StockNotice stockNotice) {
        this.context = context;
        this.stockNotice = stockNotice;
    }

    public StockNotice getStockNotice() {
        return stockNotice;
    }

    @Override
    public StockNoticesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(context)
                .inflate(R.layout.card_view_stock_news_item, parent, false);
        return new StockNoticesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StockNoticesAdapter.ViewHolder viewHolder, int position) {
        StockNotice.ReBean reBean = stockNotice.getRe().get(position);
        viewHolder.btn_view.setTag(reBean);
        viewHolder.tv_title.setText(reBean.getTitle());
        viewHolder.tv_text.setText(reBean.getText());
        String time = reBean.getPublish_time();
        if (!StringUtil.isBlank(time)) {
            viewHolder.tv_time.setText(time);

            try {
                Date today = DateUtil.getCurDate(DateUtil.DEFAULT_DATE_FORMAT);
                Date dateTime = DateUtil.parseDate(time, DateUtil.DEFAULT_DATE_FORMAT);
                if (today.getTime() == dateTime.getTime()) {
                    LogUtil.d("时间相同");
                    Drawable img_off = ResourceManager.getResources().getDrawable(R.mipmap.today);
                    //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                    img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
                    viewHolder.tv_title.setCompoundDrawables(img_off, null, null, null); //设置左图标
                } else {
                    viewHolder.tv_title.setCompoundDrawables(null, null, null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public int getItemCount() {
        return stockNotice.getRe().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btn_view;
        public TextView tv_title;
        public TextView tv_text;
        public TextView tv_time;

        public ViewHolder(View view) {
            super(view);
            btn_view = (Button) view.findViewById(R.id.btn_view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StockNotice.ReBean reBean = (StockNotice.ReBean) v.getTag();
                    Intent intent = new Intent(BaseActivity.currentActivity, PDFViewerActivity.class);
                    intent.putExtra(BaseActivity.WEB_VIEW_LOAD_URL, reBean.getPdf_url());
                    intent.putExtra(BaseActivity.WEB_VIEW_TITLE, reBean.getTitle());
                    BaseActivity.currentActivity.startActivity(intent);
                }
            });
        }
    }
}
