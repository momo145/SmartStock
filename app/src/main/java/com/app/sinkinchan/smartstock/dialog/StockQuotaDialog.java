package com.app.sinkinchan.smartstock.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.app.App;
import com.app.sinkinchan.smartstock.dialog.base.BaseDialog;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.app.sinkinchan.smartstock.utils.ViewUtil;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.ActiveStocksPage;
import com.sinkinchan.stock.sdk.bean.StockQuota;
import com.sinkinchan.stock.sdk.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;


/**
 * Created by sinkinchan on 2015/9/14.
 */
public class StockQuotaDialog extends BaseDialog implements View.OnClickListener {

    View view = null;
    TextView btn_close;
    TextView tv_agreement_text;
    ImageView iv_loading;
    //指标
    //今开
    TextView tv_today_open;
    TextView tv_yesterday_close;
    TextView tv_height;
    TextView tv_low;
    TextView tv_up_limit;
    TextView tv_down_limit;
    TextView tv_volume;
    TextView tv_turnover;
    TextView tv_change;
    TextView tv_amplitude;
    TextView tv_total_equity;
    TextView tv_qutstanding_shares;
    TextView tv_pe;
    TextView tv_pb;
    SourceManager.StockType type;
    Object object;

    public StockQuotaDialog(Context context, Object object, String code, SourceManager.StockType type) {
        super(context, R.style.BaseDialog);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_stock_quota, null);
        setContentView(view);
        this.type = type;
        this.object = object;
        initView();
        //获取股票指标
        code = StockUtil.getInstance().formatStockCode(code, "");
        StockQuota.Param param = new StockQuota.Param(code);
        SourceManager.getInstance().getStockQuota(param.getParams(), new SourceManager.onSourceCallBack<StockQuota>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailed(String s) {
                LogUtil.d(s);
            }

            @Override
            public DataType getType() {
                return DataType.bean;
            }

            @Override
            public StockQuota getBean(Document document, String s) {
                StockQuota stockQuota = GsonUtil.getGson().fromJson(s, StockQuota.class);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.GET_STOCK_QUOTA_SUCCESS, stockQuota));
                return stockQuota;
            }
        });
    }


    @Override
    protected void init() {
        super.init();
    }

    private void initView() {
        //获取控件
        if (view != null) {
            btn_close = (TextView) view.findViewById(R.id.btn_close);
            btn_close.setOnClickListener(this);
            iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
            iv_loading.startAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.ratote_view_anmi));
            tv_today_open = (TextView) view.findViewById(R.id.tv_today_open);
            tv_yesterday_close = (TextView) view.findViewById(R.id.tv_yesterday_close);
            tv_height = (TextView) view.findViewById(R.id.tv_height);
            tv_low = (TextView) view.findViewById(R.id.tv_low);
            tv_up_limit = (TextView) view.findViewById(R.id.tv_up_limit);
            tv_down_limit = (TextView) view.findViewById(R.id.tv_down_limit);
            tv_volume = (TextView) view.findViewById(R.id.tv_volume);
            tv_turnover = (TextView) view.findViewById(R.id.tv_turnover);
            tv_change = (TextView) view.findViewById(R.id.tv_change);
            tv_amplitude = (TextView) view.findViewById(R.id.tv_amplitude);
            tv_total_equity = (TextView) view.findViewById(R.id.tv_total_equity);
            tv_qutstanding_shares = (TextView) view.findViewById(R.id.tv_qutstanding_shares);
            tv_pe = (TextView) view.findViewById(R.id.tv_pe);
            tv_pb = (TextView) view.findViewById(R.id.tv_pb);
        }

    }

    public void setUserAgreement(CharSequence charSequence) {
        tv_agreement_text.setText(charSequence);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Override
    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case GET_STOCK_QUOTA_SUCCESS:
                StockQuota stockQuota = (StockQuota) event.getArgs();
                iv_loading.clearAnimation();
                ViewUtil.setVisibility(View.GONE, iv_loading);
                ViewUtil.setVisibility(View.VISIBLE, tv_today_open, tv_yesterday_close, tv_height,
                        tv_low, tv_up_limit, tv_down_limit, tv_volume, tv_turnover, tv_change,
                        tv_amplitude, tv_total_equity, tv_qutstanding_shares, tv_pe, tv_pb);
                switch (type) {
                    case ActiveStock:
                        ActiveStocksPage.DataBean activeStocks = (ActiveStocksPage.DataBean) object;
                        setPe(stockQuota.getPe() + "");
                        setPb(stockQuota.getPb() + "");
                        double open = activeStocks.getOpen();
                        double yesterdayClose = activeStocks.getYestclose();
                        setOpen(open + "");
                        setHeight(activeStocks.getHigh() + "");
                        setLow(activeStocks.getLow() + "");
                        setUpLimit(yesterdayClose);
                        setDownLimit(yesterdayClose);
                        break;
                }
                break;
        }
    }

    private void setDownLimit(double value) {
        double upPrice = value - value / 10;
        tv_down_limit.setText(String.format(ResourceManager.getString(R.string.tv_down_limit_text), StockUtil.getInstance().convert(upPrice, 100)));
    }

    private void setUpLimit(double up) {
        double upPrice = up + up / 10;
        tv_up_limit.setText(String.format(ResourceManager.getString(R.string.tv_up_limit_text), StockUtil.getInstance().convert(upPrice, 100)));
    }

    private void setLow(String low) {
        tv_low.setText(String.format(ResourceManager.getString(R.string.tv_low_text), low));
    }

    private void setHeight(String height) {
        tv_height.setText(String.format(ResourceManager.getString(R.string.tv_height_text), height));
    }

    private void setOpen(String open) {
        tv_today_open.setText(String.format(ResourceManager.getString(R.string.today_open_text), open));
    }

    private void setPe(String pe) {
        tv_pe.setText(String.format(ResourceManager.getString(R.string.tv_pe_text), pe));

    }

    private void setPb(String pb) {
        tv_pb.setText(String.format(ResourceManager.getString(R.string.tv_pb_text), pb));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView tv_title = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                dismiss();
                break;
        }
        return false;
    }


    public interface OnCancelClick {
        void OnClick(View view);
    }

    public interface OnConfirmClick {
        void OnClick(View view);
    }


    @Override
    public void show() {

        super.show();
    }
}
