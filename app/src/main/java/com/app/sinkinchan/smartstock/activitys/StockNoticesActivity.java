package com.app.sinkinchan.smartstock.activitys;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.adapters.StockNoticesAdapter;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.utils.LoadingUtil;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.StockNotice;
import com.sinkinchan.stock.sdk.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import java.util.List;


public class StockNoticesActivity extends BaseActivity {

    XRecyclerView news_recyclerView;
    String code, name;
    LoadingUtil loadingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_news);
        loadingUtil = LoadingUtil.newInstance(this);
        init();
    }

    LinearLayoutManager linearLayoutManager = null;
    StockNoticesAdapter adapter;

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        news_recyclerView = (XRecyclerView) findViewById(R.id.news_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_recyclerView.setLayoutManager(linearLayoutManager);
        //设置Item增加、移除动画
        news_recyclerView.setItemAnimator(new DefaultItemAnimator());
        news_recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        news_recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNotices();
            }

            @Override
            public void onLoadMore() {

            }
        });
        getNotices();
    }


    private void getNotices() {
        StockNotice.Param param = new StockNotice.Param(code, 30);
        SourceManager.getInstance().getStockNoticeSource(param.getParams(), new SourceManager.onSourceCallBack<StockNotice>() {
            public void onSuccess(String data) {

            }

            public void onFailed(String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingUtil.showReload(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingUtil.showLoading();
                                getNotices();
                            }
                        });
                        loadingUtil.hide();
                        news_recyclerView.refreshComplete();
                        news_recyclerView.loadMoreComplete();
                        news_recyclerView.setVisibility(View.GONE);
                    }
                });
            }

            public DataType getType() {
                return DataType.bean;
            }

            public StockNotice getBean(Document document, String json) {
                StockNotice stockNotice = GsonUtil.getGson().fromJson(json, StockNotice.class);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.GET_STOCK_NOTICES_SUCCESS, stockNotice));
                return stockNotice;
            }
        });
    }

    @Override
    protected void init() {
        code = getIntent().getStringExtra(STOCK_CODE_KEY);
        name = getIntent().getStringExtra(STOCK_NAME_KEY);
        code = StockUtil.getInstance().formatStockCode(code, "");
        initRecycleView();
        setTitle(name + "的公告");
        setBackMenu();
    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }

    @Override
    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case GET_STOCK_NOTICES_SUCCESS:
                StockNotice notices = (StockNotice) event.getArgs();
                if (notices != null) {
                    loadingUtil.hide();
                    if (adapter == null) {
                        adapter = new StockNoticesAdapter(StockNoticesActivity.this, notices);
                        news_recyclerView.setAdapter(adapter);
                    } else {
                        List<StockNotice.ReBean> reBeans = adapter.getStockNotice().getRe();
                        reBeans.clear();
                        reBeans.addAll(notices.getRe());
                        adapter.notifyDataSetChanged();
                    }

                }
                if (news_recyclerView.getVisibility() != View.VISIBLE) {
                    news_recyclerView.setVisibility(View.VISIBLE);
                }
                news_recyclerView.refreshComplete();
                break;
        }
    }


}
