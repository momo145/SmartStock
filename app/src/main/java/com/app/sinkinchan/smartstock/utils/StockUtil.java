package com.app.sinkinchan.smartstock.utils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.HistoryPriceActivity;
import com.app.sinkinchan.smartstock.activitys.StockHandicapMoreActivity;
import com.app.sinkinchan.smartstock.activitys.StockNewsActivity;
import com.app.sinkinchan.smartstock.activitys.StockNoticesActivity;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.adapters.base.BaseAdapter;
import com.app.sinkinchan.smartstock.bean.DeleteStockBean;
import com.app.sinkinchan.smartstock.dialog.AdDialog;
import com.app.sinkinchan.smartstock.dialog.DeleteStockDialog;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.mode.StockHandicapModeView;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.ActiveStocksPage;
import com.sinkinchan.stock.sdk.bean.DeathSquadStockPage;
import com.sinkinchan.stock.sdk.bean.QuotaStockPage;
import com.sinkinchan.stock.sdk.bean.ResearchStockPage;
import com.sinkinchan.stock.sdk.bean.StockDetail;
import com.sinkinchan.stock.sdk.bean.StockHandicap;
import com.sinkinchan.stock.sdk.source.impls.HandicapSource;
import com.sinkinchan.stock.sdk.utils.GsonUtil;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.STOCK_HANDICAP;
import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.STOCK_HANDICAP_MORE;
import static com.sinkinchan.stock.sdk.SourceManager.onSourceCallBack.DataType.json;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-05 下午8:16
 **/
public class StockUtil {
    private static StockUtil ourInstance = new StockUtil();

    public static StockUtil getInstance() {
        return ourInstance;
    }

    private StockUtil() {
    }

    //更新股票详情线程
    private static Timer updateStockDetailTimer = null;

    public void startUpdateStockDetailTimer(final XRecyclerView recyclerView, final LinearLayoutManager linearLayoutManager, final BaseAdapter adapter) {
        try {
            stopUpdateStockDetailTimer();
            if (updateStockDetailTimer == null) {
                LogUtil.d("更新股票详情线程----开始");
                updateStockDetailTimer = new Timer("更新股票详情线程");
                updateStockDetailTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (adapter == null && adapter.isRefreshing() && adapter.isLoading()) {
                            return;
                        }
                        if (linearLayoutManager == null) {
                            return;
                        }
                        int firstIndex = linearLayoutManager.findFirstVisibleItemPosition() - 1;
                        int lastIndex = linearLayoutManager.findLastVisibleItemPosition() - 1;
                        if (firstIndex < 0 || lastIndex < 0) {
                            return;
                        }
                        List<String> codes = new ArrayList<String>();
                        switch (adapter.getType()) {
                            case ActiveStock:
                                codes = getActiveStockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case ResearchStock:
                                codes = getResearchStockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case DeathSquadStock:
                                codes = getDeathSqueadStockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case QuotaStockType1:
                                codes = getQuotaType1StockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case QuotaStockType2:
                                codes = getQuotaType2StockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case QuotaStockType3:
                                codes = getQuotaType3StockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;
                            case QuotaStockType4:
                                codes = getQuotaType4StockCodes(firstIndex, lastIndex, adapter.getPage());
                                break;

                        }
                        //获取数据
                        SourceManager.getInstance().getStockDetail(codes, new SourceManager.onSourceCallBack<Map<String, StockDetail>>() {

                            public void onFailed(String errorMsg) {
                                System.out.println(errorMsg);
                            }


                            public DataType getType() {
                                return DataType.bean;
                            }

                            public void onSuccess(String data) {

                            }

                            public Map<String, StockDetail> getBean(Document document, String json) {
                                Map<String, StockDetail> details = GsonUtil.getGson().fromJson(json, new TypeToken<Map<String, StockDetail>>() {
                                }.getType());
                                if (details != null) {
                                    if (!adapter.isRefreshing() && !adapter.isLoading()) {
                                        for (String key : details.keySet()) {
                                            StockDetail detail = details.get(key);
                                            String code = (detail.getType() + detail.getSymbol()).toLowerCase();
                                            int index = -1;
                                            switch (adapter.getType()) {
                                                case ActiveStock:
                                                    index = updateActiveStock(adapter.getPage(), code, detail);
                                                    break;
                                                case ResearchStock:
                                                    index = updateResearchStock(adapter.getPage(), code, detail);
                                                    break;
                                                case DeathSquadStock:
                                                    index = updateDeathSquadStock(adapter.getPage(), code, detail);
                                                    break;
                                                case QuotaStockType1:
                                                    index = updateQuotaType1Stock(adapter.getPage(), code, detail);
                                                    break;
                                                case QuotaStockType2:
                                                    index = updateQuotaType2Stock(adapter.getPage(), code, detail);
                                                    break;
                                                case QuotaStockType3:
                                                    index = updateQuotaType3Stock(adapter.getPage(), code, detail);
                                                    break;
                                                case QuotaStockType4:
                                                    index = updateQuotaType4Stock(adapter.getPage(), code, detail);
                                                    break;
                                            }
                                            if (!adapter.isRefreshing() && !adapter.isLoading() && index != -1) {
                                                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.UPDATE_STOCK_DETAIL, index));
                                            }
                                        }
                                    }
                                }
                                return details;
                            }
                        });
                    }
                }, 0, 1000);
            }
        } catch (Throwable throwable) {
            LogUtil.d(throwable.getMessage());
        }
    }


    /**
     * 获取活跃股的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getActiveStockCodes(int firstIndex, int lastIndex, Object object) {
        ActiveStocksPage page = (ActiveStocksPage) object;
        List<String> codes = new ArrayList<String>();
        final List<ActiveStocksPage.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    ActiveStocksPage.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取研报股的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getResearchStockCodes(int firstIndex, int lastIndex, Object object) {
        ResearchStockPage page = (ResearchStockPage) object;
        List<String> codes = new ArrayList<String>();
        final List<ResearchStockPage.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    ResearchStockPage.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取敢死队股的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getDeathSqueadStockCodes(int firstIndex, int lastIndex, Object object) {
        DeathSquadStockPage page = (DeathSquadStockPage) object;
        List<String> codes = new ArrayList<String>();
        final List<DeathSquadStockPage.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    DeathSquadStockPage.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取指标股-曙光初现的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getQuotaType1StockCodes(int firstIndex, int lastIndex, Object object) {
        QuotaStockPage.QuotaStockType1 page = (QuotaStockPage.QuotaStockType1) object;
        List<String> codes = new ArrayList<String>();
        final List<QuotaStockPage.QuotaStockType1.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    QuotaStockPage.QuotaStockType1.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取指标股-空中加油的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getQuotaType2StockCodes(int firstIndex, int lastIndex, Object object) {
        QuotaStockPage.QuotaStockType2 page = (QuotaStockPage.QuotaStockType2) object;
        List<String> codes = new ArrayList<String>();
        final List<QuotaStockPage.QuotaStockType2.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    QuotaStockPage.QuotaStockType2.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取指标股-超级短线的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getQuotaType3StockCodes(int firstIndex, int lastIndex, Object object) {
        QuotaStockPage.QuotaStockType3 page = (QuotaStockPage.QuotaStockType3) object;
        List<String> codes = new ArrayList<String>();
        final List<QuotaStockPage.QuotaStockType3.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    QuotaStockPage.QuotaStockType3.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }

    /**
     * 获取指标股-蓄势待发的信息
     *
     * @param firstIndex
     * @param lastIndex
     * @param object
     * @return
     */
    private List<String> getQuotaType4StockCodes(int firstIndex, int lastIndex, Object object) {
        QuotaStockPage.QuotaStockType4 page = (QuotaStockPage.QuotaStockType4) object;
        List<String> codes = new ArrayList<String>();
        final List<QuotaStockPage.QuotaStockType4.DataBean> dataBeanList = page.getData();
        if (page != null && dataBeanList != null) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                if (dataBeanList.size() > i) {
                    QuotaStockPage.QuotaStockType4.DataBean item = dataBeanList.get(i);
                    if (item != null && !StringUtil.isBlank(item.getName()) && !StringUtil.isBlank(item.getCode())) {
                        String code = formatStockCode(item.getCode());
//                                        LogUtil.d("VisibleItem=" + code);
                        codes.add(code);
                    }
                }
            }
        }
        return codes;
    }


    /**
     * 更新活跃股的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateActiveStock(Object object, String code, StockDetail detail) {
        ActiveStocksPage page = (ActiveStocksPage) object;
        ActiveStocksPage.DataBean bean = new ActiveStocksPage.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setHs_zf(detail.getPercent());
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        bean.setYestclose(detail.getYestclose());
        return index;
    }

    /**
     * 更新活跃股的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateResearchStock(Object object, String code, StockDetail detail) {
        ResearchStockPage page = (ResearchStockPage) object;
        ResearchStockPage.DataBean bean = new ResearchStockPage.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    /**
     * 更新活跃股的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateDeathSquadStock(Object object, String code, StockDetail detail) {
        DeathSquadStockPage page = (DeathSquadStockPage) object;
        DeathSquadStockPage.DataBean bean = new DeathSquadStockPage.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    /**
     * 更新指标股-曙光初现的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateQuotaType1Stock(Object object, String code, StockDetail detail) {
        QuotaStockPage.QuotaStockType1 page = (QuotaStockPage.QuotaStockType1) object;
        QuotaStockPage.QuotaStockType1.DataBean bean = new QuotaStockPage.QuotaStockType1.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    /**
     * 更新指标股-空中加油的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateQuotaType2Stock(Object object, String code, StockDetail detail) {
        QuotaStockPage.QuotaStockType2 page = (QuotaStockPage.QuotaStockType2) object;
        QuotaStockPage.QuotaStockType2.DataBean bean = new QuotaStockPage.QuotaStockType2.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    /**
     * 更新指标股-超级短线的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateQuotaType3Stock(Object object, String code, StockDetail detail) {
        QuotaStockPage.QuotaStockType3 page = (QuotaStockPage.QuotaStockType3) object;
        QuotaStockPage.QuotaStockType3.DataBean bean = new QuotaStockPage.QuotaStockType3.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    /**
     * 更新指标股-蓄势待发的信息
     *
     * @param object
     * @param code
     * @param detail
     * @return
     */
    private int updateQuotaType4Stock(Object object, String code, StockDetail detail) {
        QuotaStockPage.QuotaStockType4 page = (QuotaStockPage.QuotaStockType4) object;
        QuotaStockPage.QuotaStockType4.DataBean bean = new QuotaStockPage.QuotaStockType4.DataBean();
        bean.setCode(code);
        int index = page.getData().indexOf(bean);
        bean = page.getData().get(index);
        bean.setPrice(detail.getPrice());
        bean.setUpdown(detail.getUpdown());
        bean.setHigh(detail.getHigh());
        bean.setLow(detail.getLow());
        bean.setOpen(detail.getOpen());
        bean.setVolume(detail.getVolume());
        bean.setTurnover(detail.getTurnover());
        bean.setPercent(detail.getPercent());
        return index;
    }

    public void stopUpdateStockDetailTimer() {
        if (updateStockDetailTimer != null) {
            LogUtil.d("更新股票详情线程----结束");
            EventBus.getDefault().removeAllStickyEvents();
            updateStockDetailTimer.cancel();
            updateStockDetailTimer = null;
        }
    }

    private static Timer getStockHandicapTimer = null;

    /**
     * 开始获取盘口信息线程
     */
    public void startGetStockHandicapTimer(final StockHandicapModeView.StockHandicap stockHandicap, final String code) {
        LogUtil.d("获取盘口信息线程----开始");
        stopGetStockHandicapTimer();
        if (getStockHandicapTimer == null) {
            getStockHandicapTimer = new Timer("获取盘口信息线程");
            getStockHandicapTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SourceManager.getInstance().getDFCFHandicapSource(code, new SourceManager.onSourceCallBack<StockHandicap>() {
                        public void onSuccess(String json) {
                            if (json.indexOf("{") != -1) {
                                json = json.replace("{", "");
                            }
                            if (json.indexOf("}") != -1) {
                                json = json.replace("}", "");
                            }
                            List<List<String>> newList = GsonUtil.getGson().fromJson(json, new TypeToken<List<List<String>>>() {
                            }.getType());
                            String data = newList.get(0).get(0);
//                            System.out.println("data=" + data);
                            String[] benData = data.split(",");
//                            System.out.println("benData=" + benData.length);
                            List<String> baseData = Arrays.asList(benData);
                            StockHandicap source = StockHandicap.getInstance(HandicapSource.HandicapType.DFCF, baseData);
                            source2Cap(source, stockHandicap);
                        }

                        public void onFailed(String errorMsg) {
                            System.out.println(errorMsg);
                        }

                        public DataType getType() {
                            return json;
                        }

                        public StockHandicap getBean(Document document, String json) {
                            return null;
                        }
                    });
                }
            }, 0, 2000);
        }
    }

    private void source2Cap(StockHandicap source, StockHandicapModeView.StockHandicap cap) {
        if (source == null || cap == null) {
            return;
        }
        cap.code.set(source.code);
        /**
         * 股票名字
         */
        cap.name.set(source.name);
        /**
         * 最新价格
         */
        cap.latestPrice.set(source.latestPrice);
        /**
         * 涨幅
         */
        cap.up.set(source.up);
        /**
         /**
         * 涨跌
         */
        cap.upOrDown.set(source.upOrDown);
        /**
         * 换手率
         */
        cap.change.set(source.change);
        /**
         * 最高
         */
        cap.highest.set(source.highest);
        /**
         * 最低
         */
        cap.minimum.set(source.minimum);
        /**
         * 成交量
         */
        cap.dealQuantity.set(source.dealQuantity);
        /**
         * 成交金额
         */
        cap.totalDealAmount.set(source.totalDealAmount);
        /**
         * 涨停价格
         */
        cap.limitUp.set(source.limitUp);
        /**
         * 跌停价格
         */
        cap.limitDown.set(source.limitDown);
        /**
         * 昨天收市价格
         */
        cap.yesterdayPrice.set(source.yesterdayPrice);
        /**
         * 现手
         */
        cap.currentNumber.set(source.currentNumber);
        /**
         * 外盘
         */
        cap.outerDisc.set(source.outerDisc);
        /**
         * 内盘
         */
        cap.insideDish.set(source.insideDish);
        /**
         * 量比
         */
        cap.volumeRatioIndex.set(source.volumeRatioIndex);
        /**
         * 收益(季度)
         */
        cap.income.set(source.income);
        /**
         * 市盈率 动
         */
        cap.PERatiosDynamic.set(source.PERatiosDynamic);

        /**
         * 市盈率 静
         */
        cap.PERatios.set(source.PERatios);
        /**
         * 净资产
         */
        cap.netAssets.set(source.netAssets);
        /**
         * 市净率
         */
        cap.PBRatio.set(source.PBRatio);
        /**
         * 总股本
         */
        cap.generalCapital.set(source.generalCapital);
        /**
         * 总市值
         */
        cap.capitalization.set(source.capitalization);
        /**
         * 流通股本
         */
        cap.circulationOfCapitalStock.set(source.circulationOfCapitalStock);
        /**
         * 流通市值
         */
        cap.circulationOfCapitalization.set(source.circulationOfCapitalization);
        /**
         * 时间
         */
        cap.time.set(source.time);
        /**
         * 开盘价
         */
//        cap.open.set(source.open);
        /**
         * 主力流入
         */
        cap.zllr.set(source.zllr);
        /**
         * 主力流出
         */
        cap.zllc.set(source.zllc);
        /**
         * 主力净流入
         */
        cap.zljlr.set(source.zljlr);
        /**
         * 超大单
         */
        cap.cdd.set(source.cdd);
        /**
         * 大单
         */
        cap.dd.set(source.dd);
        /**
         * 中单
         */
        cap.zd.set(source.zd);
        /**
         * 小单
         */
        cap.xd.set(source.xd);
        /**
         * 流入金额单位
         */
        cap.unit.set(source.unit);
    }

    public void stopGetStockHandicapTimer() {
        if (getStockHandicapTimer != null) {
            LogUtil.d("获取盘口信息线程----结束");
            EventBus.getDefault().removeAllStickyEvents();
            getStockHandicapTimer.cancel();
            getStockHandicapTimer = null;
        }
    }


    /**
     * 格式化股票代码
     *
     * @param code
     * @return
     */
    public String formatStockCode(String code) {
        if (StringUtil.isBlank(code)) {
            return null;
        }
        code = code.toLowerCase();
        if (code.contains("sh")) {
            code = code.replace("sh", "0");
        } else if (code.contains("sz")) {
            code = code.replace("sz", "1");
        }
        return code;
    }

    /**
     * 格式化股票代码 用于F10
     *
     * @param code
     * @return
     */
    public String formatStockCode2F10(String code) {
        if (StringUtil.isBlank(code)) {
            return null;
        }
        code = code.toLowerCase();
        if (code.contains("sh")) {
            code = code.replace("sh", "");
            code += "01";
        } else if (code.contains("sz")) {
            code = code.replace("sz", "");
            code += "02";
        }
        return code;
    }

    /**
     * 格式化股票代码
     *
     * @param code
     * @return
     */
    public String formatStockCode(String code, String format) {
        if (StringUtil.isBlank(code)) {
            return null;
        }
        code = code.toLowerCase();
        if (code.contains("sh")) {
            code = code.replace("sh", format);
        } else if (code.contains("sz")) {
            code = code.replace("sz", format);
        }
        return code;
    }

    //打开浏览器 url
    public void openBrowser(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public View getHeaderView(Context context, SourceManager.StockType type) {
        View headerView = LayoutInflater.from(context).inflate(R.layout.stock_title_item, null);
        TextView title = (TextView) headerView.findViewById(R.id.tv_title);
        TextView tv_message = (TextView) headerView.findViewById(R.id.tv_message);
        title.setText(type.title);
        tv_message.setText(type.desc);
        return headerView;
    }

    /**
     * @param num
     * @return
     */
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");

    public String moneyConvert(double num) {
        String str = num + "";
        if (num > 100000) {

            double n = num / 1000000;
            str = df.format(n) + "万";
        }
        return str;
    }

    public String moneyConvert2(double num) {
        String str = num + "";
        if (num > 10000000) {
            double n = num / 100000000;
            str = df.format(n) + "亿";
        } else {
            str = moneyConvert(num);
        }
        return str;
    }


    public double convert(double value, int index) {
        long l1 = Math.round(value * index);   //四舍五入
        double ret = l1 / 100.0;               //注意：使用   100.0   而不是   100
        return ret;
    }

    //根据股票代码获取市场
    public String getMessageCode(String code) {
        String i = code.substring(0, 1);
        String j = code.substring(0, 3);
        if (i == "5" || i == "6" || i == "9") {
            return "1" + code; //上证股票
        } else {
            if (j == "009" || j == "126" || j == "110") {
                return "1" + code; //上证股票
            } else {
                return "0" + code; //深圳股票
            }
        }
    }

    public String formatStockHandicapCode(String code) {
        code = code.toLowerCase();
        if (code.contains("sh")) {
            code = code.replace("sh", "");
            code += 1;
        } else if (code.contains("sz")) {
            code = code.replace("sz", "");
            code += 2;
        }
        return code;
    }

    public CharSequence zllr(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        String html = "主力流入: <font color='#e53935'>%s</font>";
        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence zllc(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        String html = "主力流出: <font color='#4caf50'>%s</font>";
        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence zljlr(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        Float strFloat = Float.valueOf(str);
        String html = "主力净流入: <font color='#4caf50'>%s</font>";
        if (strFloat > 0) {
            html = "主力净流入: <font color='#e53935'>%s</font>";
        }

        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence cdd(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        Float strFloat = Float.valueOf(str);
        String html = "超大单: <font color='#4caf50'>%s</font>";
        if (strFloat > 0) {
            html = "超大单: <font color='#e53935'>%s</font>";
        }

        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence dd(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        Float strFloat = Float.valueOf(str);
        String html = "大单: <font color='#4caf50'>%s</font>";
        if (strFloat > 0) {
            html = "大单: <font color='#e53935'>%s</font>";
        }

        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence zd(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        Float strFloat = Float.valueOf(str);
        String html = "中单: <font color='#4caf50'>%s</font>";
        if (strFloat > 0) {
            html = "中单: <font color='#e53935'>%s</font>";
        }

        return Html.fromHtml(String.format(html, str));
    }

    public CharSequence xd(String str) {
        if (StringUtils.isBlank(str)) {
            str = "0";
        }
        Float strFloat = Float.valueOf(str);
        String html = "小单: <font color='#4caf50'>%s</font>";
        if (strFloat > 0) {
            html = "小单: <font color='#e53935'>%s</font>";
        }

        return Html.fromHtml(String.format(html, str));
    }

    /**
     * 格式化大数目
     * 过万的按万  过亿的按亿单位，然后显示时候小数点前数字两个以内的保留两位小数，三个的保留一个，四个的不保留小数点
     *
     * @param value
     * @return
     */
    public String formatValue(String value) {
        if (!isNumeric(value)) {
            return null;
        }
        double val = Double.valueOf(value);
        String unit = "亿";
        if (val >= 1e4) {
            val = Double.valueOf(val);
            if (val >= 1e8) {
                val = val / 1e8;
                int valInt = (int) val;
                if (valInt >= 1e3) {
                    val = valInt;
                } else if (valInt >= 100) {
                    value = df.format(val);
                } else {
                    value = df.format(val);
                }

            } else {
                val = val / 1e4;
                int valInt = (int) val;
                if (valInt >= 1e3) {
                    value = df.format(valInt);
                } else if (valInt >= 100) {
                    value = df.format(val);
                } else {
                    value = df.format(val);
                }
                unit = "万";
            }
            return value + unit;
        } else {
            return value;
        }
    }


    public boolean isNumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取换手率
     *
     * @param change
     * @return
     */
    public String getChange(double change) {
        String str = String.format(ResourceManager.getString(R.string.change_percent_text), String.valueOf(convert(change, 10000)));
        return str;
    }

    /**
     * 获取涨幅
     *
     * @param percent
     * @param zf
     * @return
     */
    public String getUpOrDown(double Updown, double percent, double zf) {
        String str = "";
        if (percent > 0) {
            str = convert(percent, 10000) + "%";
        } else {
            str = convert(zf, 10000) + "%";
        }
        return Updown + " " + str;
    }

    /**
     * 判断是涨还是跌
     *
     * @param percent
     * @return
     */
    public boolean isUp(double percent) {
        if (percent >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取当日最高价格
     *
     * @param high
     * @return
     */
    public String getHigh(double high) {
        return String.format(ResourceManager.getString(R.string.high_percent_text), String.valueOf(high));
    }

    /**
     * 获取当日最低价格
     *
     * @param low
     * @return
     */
    public String getLow(double low) {
        return String.format(ResourceManager.getString(R.string.low_percent_text), String.valueOf(low));
    }

    /**
     * 获取当日开盘价格
     *
     * @param open
     * @return
     */
    public String getOpen(double open) {
        return String.format(ResourceManager.getString(R.string.open_percent_text), String.valueOf(open));
    }

    /**
     * 获取当日量
     *
     * @param volume
     * @return
     */
    public String getVolume(double volume) {
        return String.format(ResourceManager.getString(R.string.volume_percent_text), moneyConvert(volume));
    }

    /**
     * 获取当日量
     *
     * @param turnover
     * @return
     */
    public String getTurnover(double turnover) {
        return String.format(ResourceManager.getString(R.string.turnover_percent_text), moneyConvert2(turnover));
    }

    /**
     * 弹出更多
     *
     * @param v
     * @param code
     * @param name
     */
    public void showMoreMenu(final View v, String code, String name) {
        Log.d("showMoreMenu", "showMoreMenu: code=" + code + " name=" + name);
        PopupMenu popup = new PopupMenu(BaseActivity.currentActivity, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_stock_more_menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.news:
                        Intent intent = new Intent(BaseActivity.currentActivity, StockNewsActivity.class);
                        intent.putExtra(BaseActivity.STOCK_CODE_KEY, code);
                        intent.putExtra(BaseActivity.STOCK_NAME_KEY, name);
                        BaseActivity.currentActivity.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Show the menu
        popup.show();
    }

    /**
     * 删除股票
     *
     * @param v
     * @param code
     * @param name
     */
    public void deleteStock(final View v, String code, String name) {
//        final ActiveStocksPage.DataBean dataBean = (ActiveStocksPage.DataBean) v.getTag();
        DeleteStockDialog deleteStockDialog = new DeleteStockDialog();
        deleteStockDialog.setTitle(ResourceManager.getString(R.string.dialog_title_text))
                .setMessage(String.format(ResourceManager.getString(R.string.delete_stock_msg_text)
                        , name))
                .setNegativeButtonText(ResourceManager.getString(R.string.dialog_cancel_text))
                .setPositiveButtonText(ResourceManager.getString(R.string.dialog_delete_text))
                .setPositiveButtonListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteStockBean deleteStockBean = new DeleteStockBean();
                        deleteStockBean.setStockCode(code);
                        try {
                            deleteStockBean.setTime(DateUtil.getDeleteExpiredTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        deleteStockBean.setType(SourceManager.StockType.ActiveStock.name());
                        deleteStockBean.save();
//                        activeStocksPage.getData().remove(dataBean);
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.DELETE_ITEM, code));
                    }
                }).show();
    }

    /**
     * 显示历史价格
     *
     * @param v
     * @param code
     * @param name
     */
    public void showHistoryPrice(final View v, String code, String name) {
        Intent intent = new Intent(BaseActivity.currentActivity, HistoryPriceActivity.class);
        intent.putExtra(BaseActivity.STOCK_CODE_KEY, code);
        intent.putExtra(BaseActivity.STOCK_NAME_KEY, name);
        BaseActivity.currentActivity.startActivity(intent);
    }

    /**
     * 显示公告
     *
     * @param v
     * @param code
     * @param name
     */
    public void showNotices(final View v, String code, String name) {
        Intent intent = new Intent(BaseActivity.currentActivity, StockNoticesActivity.class);
        intent.putExtra(BaseActivity.STOCK_CODE_KEY, code);
        intent.putExtra(BaseActivity.STOCK_NAME_KEY, name);
        BaseActivity.currentActivity.startActivity(intent);
    }

    /**
     * 显示股票详情
     *
     * @param cardView
     */
    public void showStockDetail(View cardView, String code, String name, String open) {
        cardView.setTag(R.id.stock_handicap_code, code);
        cardView.setTag(R.id.stock_handicap_name, name);
        cardView.setTag(R.id.stock_handicap_open, open);
        cardView.setTag(R.id.stock_is_show_ad, isShowAD(code));
        EventBus.getDefault().post(new MessageEvent(STOCK_HANDICAP, cardView));
    }

    static final String messageStr = "(1)%s MACD金叉\n(2)%s 成交量金叉\n(3)%s 均线金叉";

    /**
     * 显示曙光初现的信息
     *
     * @param macd
     * @param vol
     * @param close
     * @return
     */
    public String showQuotaType1Message(String macd, String vol, String close) {
        return String.format(messageStr, macd, vol, close);
    }

    static final String QuotaType2Message = "(1)20日和60日均线多头排列 \n(2)%s 凯恩斯多空线金叉";

    /**
     * 显示空中加油的信息
     *
     * @param kns
     * @return
     */
    public String showQuotaType2Message(String kns) {
        return String.format(QuotaType2Message, kns);
    }

    static final String QuotaType4Message = "(1)60日涨幅%s\n(2)最新价%s元";

    /**
     * 显示蓄势待发的信息
     *
     * @return
     */
    public String showQuotaType4Message(double zf60, String price) {
        ;
        return String.format(QuotaType4Message, convert(zf60, 10000) + "%", price);
    }

    final String ResearchStockMessage = "%s    %s    %s  %s";

    /**
     * 显示研报股的信息
     *
     * @return
     */
    public String showResearchStockMessage(String mbjg, String fbrq, String jgjc, String tzpj) {
        if (StringUtil.isBlank(mbjg)) {
            mbjg = "无目标价格";
        } else {
            mbjg = df.format(Double.valueOf(mbjg));
        }
        return String.format(ResearchStockMessage, fbrq, jgjc, tzpj, mbjg);
    }

    static final String DeathSquadStockMessage = "(1)营业部:%s\n(2)上榜频次%s";

    /**
     * @return
     */
    public String showDeathSquadStockMessage(Map<String, String> yyb) {
        String msg = "";
        if (yyb != null && yyb.size() > 0) {
            Set<String> keySet = yyb.keySet();
            for (String key : keySet) {
                msg = String.format(DeathSquadStockMessage, key, convert(Double.valueOf(yyb.get(key)), 10000) + "%");
            }
        }
        return msg;
    }

    //    Random random = null;
    Map<String, Boolean> adMap = new HashMap<>();

    /**
     * 是否显示广告
     *
     * @return
     */
    public boolean isShowAD(String code) {
        Boolean isShow = adMap.get(code);
        if (isShow == null) {
            int index = RandomUtils.nextInt(0, 100);
            isShow = index <= 30;
            adMap.put(code, isShow);
        }
        return isShow;
    }

    /**
     * 显示广告
     */
    public void showAD() {
        AdDialog adDialog = new AdDialog();
        adDialog.show(BaseActivity.currentActivity);
    }

    String clickText = ResourceManager.getString(R.string.stock_handicap_more_text);
    String productTitle = ResourceManager.getString(R.string.stock_main_product_text);
    String productTextTemp = null;
    SpannableString productString;
    public boolean isShowMore = false;

    /**
     * 获取主营产品
     *
     * @param products
     * @return
     */
    public CharSequence getProducts(List<String> products) {

        if (products != null && products.size() > 0) {
            String contents = null;
            for (int i = 0; i < products.size(); i++) {
                if (i >= 2) {
                    break;
                }
                if (StringUtils.isBlank(contents)) {
                    contents = products.get(i) + " ";
                } else {
                    contents += products.get(i) + " ";
                }

            }
            String productText = productTitle + contents + clickText;
            //防止多次进入,损耗性能
            if (StringUtils.equals(productTextTemp, productText)) {
                return productString;
            }
            productTextTemp = productText;
            productString = new SpannableString(productText);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    StockHandicapMoreActivity.list = products;
                    StockHandicapMoreActivity.title = ResourceManager.getString(R.string.stock_main_product_text).replace(":", "");
                    EventBus.getDefault().post(new MessageEvent(STOCK_HANDICAP_MORE, widget));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    /*ds.setColor(getResources().getColor(android.R.color.holo_red_dark));
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();*/
                }
            };
            productString.setSpan(clickableSpan, productText.length() - clickText.length(), productText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return productString;
        }
        return ResourceManager.getString(R.string.stock_main_product_text) + ResourceManager.getString(R.string.empty_text);
    }

    String conceptTitle = ResourceManager.getString(R.string.stock_concept_text);
    String conceptTextTemp = null;
    SpannableString conceptString;

    /**
     * 获取所属概念
     *
     * @param concepts
     * @return
     */
    public CharSequence getConcepts(List<String> concepts) {
        if (concepts != null && concepts.size() > 0) {
            String contents = null;
            for (int i = 0; i < concepts.size(); i++) {
                if (i > 2) {
                    break;
                }
                if (StringUtils.isBlank(contents)) {
                    contents = concepts.get(i) + " ";
                } else {
                    contents += concepts.get(i) + " ";
                }

            }
            String conceptText = conceptTitle + contents + " " + clickText;
            //防止多次进入,损耗性能
            if (StringUtils.equals(conceptTextTemp, conceptText)) {
                return conceptString;
            }
            conceptTextTemp = conceptText;
            conceptString = new SpannableString(conceptText);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    StockHandicapMoreActivity.list = concepts;
                    StockHandicapMoreActivity.title = ResourceManager.getString(R.string.stock_concept_text).replace(":", "");
                    EventBus.getDefault().post(new MessageEvent(STOCK_HANDICAP_MORE, widget));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    /*ds.setColor(getResources().getColor(android.R.color.holo_red_dark));
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();*/
                }
            };
            conceptString.setSpan(clickableSpan, conceptText.length() - clickText.length(), conceptText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return conceptString;
        }
        return ResourceManager.getString(R.string.stock_concept_text) + ResourceManager.getString(R.string.empty_text);
    }

}

