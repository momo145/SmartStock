package com.app.sinkinchan.smartstock.activitys;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.databinding.ActivityStockHandicapBinding;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.mode.StockHandicapModeView;
import com.app.sinkinchan.smartstock.utils.APILevel;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.StockClassify;
import com.sinkinchan.stock.sdk.bean.StockF10;
import com.sinkinchan.stock.sdk.utils.GsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.STOCK_HANDICAP_MORE;


public class StockHandicapActivity extends BaseActivity {
    String code, name, open;
    ActivityStockHandicapBinding binding;
    //    StockHandicap stockHandicap;
    StockHandicapModeView.StockHandicap cap = new StockHandicapModeView.StockHandicap();
    public static CardView cardView;
    public static final int STOCK_CODE_KEY = 1000;
    public static final int STOCK_NAME_KEY = 1001;
    boolean isShowAd = true;
    StockHandicapModeView modeView = new StockHandicapModeView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_handicap);
        binding.setStockUtil(StockUtil.getInstance());
        binding.setModeView(modeView);
        modeView.cap.set(cap);
        // 获取广告条
        /*View bannerView = BannerManager.getInstance(this)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                        LogUtil.d("获取广告条成功.");
                    }

                    @Override
                    public void onSwitchBanner() {
                        LogUtil.d("onSwitchBanner.");
                    }

                    @Override
                    public void onRequestFailed() {
                        LogUtil.d("获取广告条失败.");
                    }
                });

        // 将广告条加入到布局中
        binding.llBanner.addView(bannerView);*/
        init();
    }

    MenuInflater moreItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        moreItem = getMenuInflater();
        moreItem.inflate(R.menu.stock_handicap_more_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (!StringUtils.isBlank(stockF10Code)) {
                    StockHandicapMoreActivity.list = StockF10.getInstance().getF10List();
                    StockHandicapMoreActivity.title = STOCK_HANDICAP_MORE_TITLE;
                    StockHandicapMoreActivity.code = stockF10Code;
                    StockHandicapMoreActivity.name = name;
                    EventBus.getDefault().post(new MessageEvent(STOCK_HANDICAP_MORE, item.getActionView()));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getStockClassify() {
        StockUtil.getInstance().isShowMore = false;
        binding.tvProduct.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvConcept.setMovementMethod(LinkMovementMethod.getInstance());
        SourceManager.getInstance().getStockClassify(stockClassifyCode, new SourceManager.onSourceCallBack<StockClassify>() {
            public void onSuccess(String data) {

            }

            public void onFailed(String errorMsg) {

            }

            public DataType getType() {
                return DataType.bean;
            }

            public StockClassify getBean(Document document, String json) {
                StockClassify stockClassify = GsonUtil.getGson().fromJson(json, StockClassify.class);
                StockClassify.Bean bean = stockClassify.getBean();
                modeView.classify.set(bean);
                StockUtil.getInstance().isShowMore = true;
                return stockClassify;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        StockUtil.getInstance().startGetStockHandicapTimer(cap, code);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StockUtil.getInstance().stopGetStockHandicapTimer();
    }

    //    public static Bitmap bitmap = null;
    String stockClassifyCode = null;
    String stockF10Code = null;

    @Override
    protected void init() {
        code = cardView.getTag(R.id.stock_handicap_code).toString();
        stockClassifyCode = StockUtil.getInstance().formatStockCode(code, "");
        stockF10Code = StockUtil.getInstance().formatStockCode2F10(code);
        name = cardView.getTag(R.id.stock_handicap_name).toString();
        open = cardView.getTag(R.id.stock_handicap_open).toString();
        isShowAd = (boolean) cardView.getTag(R.id.stock_is_show_ad);
        cap.open.set(open);
        code = StockUtil.getInstance().formatStockHandicapCode(code);
        setBackMenu();
        setTitle(name.replace("\n", "") + " 盘口");
        getStockClassify();
        //是否显示尾部广告
      /*  if (isShowAd) {
            binding.adLayout.setVisibility(View.VISIBLE);
        }*/
        /*if (bitmap == null) {
            final float percent = (float) 300 / activityScreenBitmap.getHeight(); // 计算以300为高度的缩放百分比
            bitmap = ViewUtil.blur(getSmallSizeBitmap(activityScreenBitmap, percent), 3f);
        }
        binding.ivBg.setImageBitmap(bitmap);
        binding.ivBg.setVisibility(View.VISIBLE);
        binding.invalidateAll();*/

    }

    private Bitmap getSmallSizeBitmap(Bitmap source, float percent) {
        if (percent > 1 || percent <= 0) {
            throw new IllegalArgumentException("percent must be > 1 and <= 0");
        }
        Matrix matrix = new Matrix();
        matrix.setScale(percent, percent);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    @Override
    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case STOCK_HANDICAP_MORE:
                final View view = (View) event.getArgs();
                Intent intent = new Intent(getApplicationContext(), StockHandicapMoreActivity.class);
                if (APILevel.requireLollipop()) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StockHandicapActivity.this);
                    if (view != null) {
                        options = ActivityOptions.makeSceneTransitionAnimation(StockHandicapActivity.this, view,
                                ResourceManager.getString(R.string.stock_handicap_more_text));
                    }
                    startActivity(intent, options.toBundle());

                } else {
                    startActivity(intent);
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
