package com.app.sinkinchan.smartstock.activitys;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.adapters.HistoryPriceAdapter;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.utils.DateUtil;
import com.app.sinkinchan.smartstock.utils.LoadingUtil;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.StockHistoryPrice;
import com.sinkinchan.stock.sdk.source.impls.StockHistoryPriceSource;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryPriceActivity extends BaseActivity {

    String code, name;
    private XRecyclerView recyclerView;
    private HistoryPriceAdapter adapter;
    LoadingUtil loadingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pirce);
        loadingUtil = LoadingUtil.newInstance(this);
        code = getIntent().getStringExtra(STOCK_CODE_KEY);
        name = getIntent().getStringExtra(STOCK_NAME_KEY);
        setBackMenu();
        setTitle(name + " 的历史价格");
        init();
    }


    LinearLayoutManager linearLayoutManager = null;
    int season = 1;

    @Override
    protected void init() {
        recyclerView = (XRecyclerView) findViewById(R.id.history_price_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScaleParty);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (season > 1) {
                    season -= 1;
                    getHistoryPrice();
                } else {
                    recyclerView.noMoreLoading();
                    recyclerView.setIsnomore(true);
                }
                LogDebug("season=" + season);
            }
        });
        code = StockUtil.getInstance().formatStockCode(code, "");
        season = DateUtil.getSeason();
        getHistoryPrice();
    }

    private void getHistoryPrice() {
        StockHistoryPrice.Param param = new StockHistoryPrice.Param(code, DateUtil.currentTime(DateUtil.DEFAULT_DATE_FORMAT_YEAR), season + "");
        SourceManager.getInstance().getStockHistoryPrice(param, new SourceManager.onSourceCallBack<List<StockHistoryPrice>>() {
            public void onFailed(String errorMsg) {
                LogUtil.d(errorMsg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingUtil.showReload(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingUtil.showLoading();
                                getHistoryPrice();
                            }
                        });
                    }
                });

            }

            public DataType getType() {
                return DataType.bean;
            }

            public void onSuccess(String data) {

            }

            public List<StockHistoryPrice> getBean(Document document, String json) {
                List<StockHistoryPrice> list = StockHistoryPriceSource.getInstance().processData(document);
                LogUtil.d(list.toString());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.GET_STOCK_HISTORY_PRICE_SUCCESS, list));
                return list;
            }
        });
    }

    MenuInflater analyzeItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        analyzeItem = getMenuInflater();
        analyzeItem.inflate(R.menu.history_price_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_analyze:
                if (priceList != null && priceList.size() > 0) {
                    StockHistoryPrice currentPrice = priceList.get(0);
                    StockHistoryPrice beforePrice = priceList.get(priceList.size() - 1);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(HistoryPriceActivity.this);
                    StockHistoryPrice maxValue = Collections.max(priceList, (price1, price2) -> Double.compare(price1.getClosePrice(), price2.getClosePrice()));
                    StockHistoryPrice minValue = Collections.min(priceList, (price1, price2) -> Double.compare(price1.getClosePrice(), price2.getClosePrice()));
                    String max = String.valueOf(maxValue.getClosePrice());
                    String min = String.valueOf(minValue.getClosePrice());
                    String currentTime = DateUtil.toString(currentPrice.getTime());
                    String beforeTime = DateUtil.toString(beforePrice.getTime());
                    String message = "从%s到%s的价格分析\n最高价 %s\n最低价 %s";
                    dialog.setTitle("价格分析").setMessage(String.format(message, beforeTime,
                            currentTime, max, min)).setPositiveButton("确定", null);
                    dialog.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    List<StockHistoryPrice> priceList = null;

    @Override
    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case GET_STOCK_HISTORY_PRICE_SUCCESS:
                List<StockHistoryPrice> list = (List<StockHistoryPrice>) event.getArgs();
                if (list != null) {
                    loadingUtil.hide();

                    if (adapter == null) {
                        priceList = new ArrayList<>();
                        priceList.addAll(list);
                        adapter = new HistoryPriceAdapter(HistoryPriceActivity.this, priceList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        priceList.addAll(list);
                        recyclerView.loadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }


                }
                break;
        }
    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }
}
