package com.app.sinkinchan.smartstock.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.databinding.ActivityStockHandicapMoreBinding;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.sinkinchan.stock.sdk.bean.StockF10;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class StockHandicapMoreActivity extends BaseActivity {

    ActivityStockHandicapMoreBinding binding;
    public static List<String> list;
    public static String title;
    public static String code;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_handicap_more);
        binding.listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
        binding.setTitle(title);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                https://zhidao.baidu.com/index/?word=粤港澳&uid=wapp_1481025931690_783&step=1
                if (StringUtils.equals(STOCK_HANDICAP_MORE_TITLE, title)) {
                    openF10(position);
                } else {
                    openSearchKeyword(position);
                }


            }
        });
        binding.btnClose.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void openF10(int position) {
        String key = list.get(position);
        String url = StockF10.getInstance().getUrl(key, code);
        Intent intent = new Intent(BaseActivity.currentActivity, WebViewActivity.class);
        intent.putExtra(BaseActivity.WEB_VIEW_TITLE, name + " " + key);
        intent.putExtra(BaseActivity.WEB_VIEW_LOAD_URL, url);
        startActivity(intent);
    }

    private void openSearchKeyword(int position) {
        try {
            Intent intent = new Intent(BaseActivity.currentActivity, WebViewActivity.class);
            String keyWord = list.get(position);
            intent.putExtra(BaseActivity.WEB_VIEW_TITLE, keyWord + " 概念");
            keyWord = URLEncoder.encode(keyWord, "UTF-8");
            String url = "https://zhidao.baidu.com/index/?word=" + keyWord + "&uid=wapp_1481025931690_783&step=1";
            intent.putExtra(BaseActivity.WEB_VIEW_LOAD_URL, url);
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {
    }


    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }

    @Override
    public void onEventMainThread(MessageEvent event) {

    }

}
