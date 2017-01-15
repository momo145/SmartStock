package com.app.sinkinchan.smartstock.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.adapters.QuotaType1StockAdapter;
import com.app.sinkinchan.smartstock.bean.DeleteStockBean;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.fragments.base.BaseFragment;
import com.app.sinkinchan.smartstock.utils.DateUtil;
import com.app.sinkinchan.smartstock.utils.LoadingUtil;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.StockUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.QuotaStockPage;
import com.sinkinchan.stock.sdk.utils.GsonUtil;
import com.sinkinchan.transport.module.TransportType;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午4:44
 **/
public class QuotaType1StockFragment extends BaseFragment {
    XRecyclerView recyclerView;
    LoadingUtil loadingUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        loadingUtil = LoadingUtil.newInstance(view);
        initRecycleView(view);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_SETTING));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SET_TITLE, SourceManager.StockType.QuotaStock.title));
        askUseFunction(TransportType.useFunction_QuotaStockType1.name());
        return view;
    }


    LinearLayoutManager linearLayoutManager = null;
    QuotaType1StockAdapter adapter = null;

    /**
     * 初始化RecycleView
     */
    boolean isLoadMore = false;

    private void initRecycleView(View view) {
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        /*View headerView = StockUtil.getInstance().getHeaderView(view.getContext(), SourceManager.StockType.ActiveStock);
        //设置Item增加、移除动画
        recyclerView.addHeaderView(headerView);*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScaleParty);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (adapter != null) {
//                    activeStockAdapter.isRefreshing = true;
                    adapter.setRefreshing(true);
                }
                getQuotaType1StocksPage(1, true);

            }

            @Override
            public void onLoadMore() {
                if (adapter != null && adapter.getQuotaStockType1() != null && param != null) {
                    int total = adapter.getQuotaStockType1().getTotal();
                    int pageSize = adapter.getQuotaStockType1().getPagesize();
                    int totalPage = 0;
                    int pageNum = param.getPage();
                    if (total % pageSize == 0) {
                        totalPage = total / pageSize;
                    } else {
                        totalPage = total / pageSize + 1;
                    }
                    if (pageNum >= totalPage) {
                        //没有更多了
                        if (recyclerView != null) {
                            recyclerView.noMoreLoading();
                            recyclerView.setIsnomore(true);
                        }
                    } else {
                        //加载下一页
                        pageNum += 1;
                        isLoadMore = true;
                        if (adapter != null) {
//                            activeStockAdapter.isLoading = true;
                            adapter.setLoading(true);
                        }
                        getQuotaType1StocksPage(pageNum, true);
                    }
                }

            }
        });
        getQuotaType1StocksPage(1, false);
//        recyclerView.setRefreshing(true);
        /*swipeRefreshLayout.post(new Runna
        ble() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        refreshListener.onRefresh();*/
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public static QuotaStockPage.Param param = null;

    //获取活跃股
    private void getQuotaType1StocksPage(int page, boolean isInitiativeRefresh) {
        if (!isInitiativeRefresh) {
            loadingUtil.showLoading();
        }
        if (param == null) {
            param = new QuotaStockPage.Param(1, 30, QuotaStockPage.Type.type1);
        } else {
            param.setPage(page);
        }

        try {
            SourceManager.getInstance().getQuotaStock(param.getParams(), new SourceManager.onSourceCallBack<QuotaStockPage.QuotaStockType1>() {
                public void onSuccess(String data) {

                }

                public void onFailed(String errorMsg) {
                    LogUtil.d(errorMsg);
                }

                public DataType getType() {
                    return DataType.bean;
                }

                public QuotaStockPage.QuotaStockType1 getBean(Document document, String json) {
                    QuotaStockPage.QuotaStockType1 type1 = GsonUtil.getGson().fromJson(json, QuotaStockPage.QuotaStockType1.class);
                    List<QuotaStockPage.QuotaStockType1.DataBean> dataBeanList = type1.getData();
                    //过滤删除的股票
                    try {
                        String now = DateUtil.today(DateUtil.DEFAULT_DATETIME_FORMAT_SEC2);
                        List<DeleteStockBean> list = DeleteStockBean.find(DeleteStockBean.class, "time > ? and type = ?",
                                now, SourceManager.StockType.QuotaStockType1.name());
                        if (list != null) {
                            for (DeleteStockBean bean : list) {
                                QuotaStockPage.QuotaStockType1.DataBean dataBean = new QuotaStockPage.QuotaStockType1.DataBean();
                                dataBean.setCode(bean.getStockCode());
                                if (dataBeanList != null && dataBeanList.contains(dataBean)) {
                                    dataBeanList.remove(dataBean);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.GET_QUOTA_STOCK_SUCCESS, type1));
                    return type1;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        LogUtil.d("onPause");
        super.onPause();
        StockUtil.getInstance().stopUpdateStockDetailTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("onResume");
        if (adapter != null) {
            StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, adapter);
        }

    }

    @Override
    public void onEventMainThread(MessageEvent event) {
        super.onEventMainThread(event);
        switch (event.getType()) {
            case GET_QUOTA_STOCK_SUCCESS:
                QuotaStockPage.QuotaStockType1 page = (QuotaStockPage.QuotaStockType1) event.getArgs();
                if (page != null) {
                    if (adapter == null) {
                        adapter = new QuotaType1StockAdapter(getActivity(), page);
                        recyclerView.setAdapter(adapter);
                        recyclerView.refreshComplete();
//                        activeStockAdapter.isRefreshing = false;
                        adapter.setRefreshing(false);
                        StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, adapter);
                        showMessage(String.format(SOURCE_DATE, page.getDate()));
                    } else {
                        if (param.getPage() == 1) {
                            adapter.getQuotaStockType1().getData().clear();
                        }
                        adapter.getQuotaStockType1().setTotal(page.getTotal());
                        adapter.getQuotaStockType1().setPagesize(page.getPagesize());
                        adapter.getQuotaStockType1().setPage(page.getPage());
                        adapter.getQuotaStockType1().setCode(page.getCode());
                        adapter.getQuotaStockType1().setDate(page.getDate());
                        if (page.getData().size() > 0) {
                            QuotaStockPage.QuotaStockType1.DataBean dataBean = page.getData().get(0);
                            if (!adapter.getQuotaStockType1().getData().contains(dataBean)) {
                                adapter.getQuotaStockType1().getData().addAll(page.getData());
                            } else {
                                LogUtil.d("异常........");
                            }
                        }
                    }
                    if (adapter.getQuotaStockType1().getData().size() == 0) {
                        loadingUtil.showNone();
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        loadingUtil.hide();
                        if (recyclerView.getVisibility() != View.VISIBLE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                    if (adapter.isLoading()) {
                        recyclerView.loadMoreComplete();
//                        activeStockAdapter.isLoading = false;
                        adapter.setLoading(false);
                    } else if (adapter.isRefreshing()) {
                        recyclerView.refreshComplete();
//                        activeStockAdapter.isRefreshing = false;
                        adapter.setRefreshing(false);
                    }
                    adapter.notifyDataSetChanged();

                }
                break;
            case UPDATE_STOCK_DETAIL:
                if (adapter != null) {
                    int index = (int) event.getArgs();
                    // TODO: 16/9/6 这里用 notifyItemChanged 更新item的时候,会导致最后一个item无法更新,暂时的解决办法是index等于最后一个的时候就使用notifyDataSetChanged
                    if (adapter != null) {
                        if (index == adapter.getQuotaStockType1().getData().size() - 1) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.notifyItemChanged(index);
                        }
                    }
                }
                break;
            case REFRESH_STOCK:
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                recyclerView.setRefreshing(true);
                break;
            case GO_TO_TOP:
                if (linearLayoutManager != null) {
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                }
                break;
            case DELETE_ITEM:
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
