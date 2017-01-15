package com.app.sinkinchan.smartstock.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.adapters.ActiveStockAdapter;
import com.app.sinkinchan.smartstock.app.App;
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
import com.sinkinchan.stock.sdk.bean.ActiveStocksPage;
import com.sinkinchan.stock.sdk.utils.GsonUtil;
import com.sinkinchan.transport.module.TransportType;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.GET_ACTIVE_STOCKS_PAGE_SUCCESS;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午4:44
 **/
public class ActiveStockFragment extends BaseFragment {
    XRecyclerView recyclerView;
    LoadingUtil loadingUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        loadingUtil = LoadingUtil.newInstance(view);
        initRecycleView(view);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_SETTING));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HIDE_TAB_LAYOUT));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SET_TITLE, SourceManager.StockType.ActiveStock.title));
        askUseFunction(TransportType.useFunction_ActiveStock.name());
//        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_POINTS_POOL_DIALOG));
        return view;
    }


    LinearLayoutManager linearLayoutManager = null;
    ActiveStockAdapter activeStockAdapter = null;


    /**
     * 初始化RecycleView
     */
    boolean isLoadMore = false;

    private void initRecycleView(View view) {
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
       /* View headerView = StockUtil.getInstance().getHeaderView(view.getContext(), SourceManager.StockType.ActiveStock);
        //设置Item增加、移除动画
        recyclerView.addHeaderView(headerView);*/
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScaleParty);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (activeStockAdapter != null) {
//                    activeStockAdapter.isRefreshing = true;
                    activeStockAdapter.setRefreshing(true);
                }
                getActiveStocksPage(1, true);

            }

            @Override
            public void onLoadMore() {
                if (activeStockAdapter != null && activeStockAdapter.getActiveStocksPage() != null && param != null) {
                    int total = activeStockAdapter.getActiveStocksPage().getTotal();
                    int pageSize = activeStockAdapter.getActiveStocksPage().getPagesize();
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
                        if (activeStockAdapter != null) {
//                            activeStockAdapter.isLoading = true;
                            activeStockAdapter.setLoading(true);
                        }
                        getActiveStocksPage(pageNum, true);
                    }
                }

            }
        });
        getActiveStocksPage(1, false);
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

    public static ActiveStocksPage.Param param = null;

    //获取活跃股
    private void getActiveStocksPage(int page, boolean isInitiativeRefresh) {
        if (!isInitiativeRefresh) {
            loadingUtil.showLoading();
        }
        if (param == null) {
            param = new ActiveStocksPage.Param(ActiveStocksPage.Param.Type.CHANGE_5, ActiveStocksPage.Param.Type.LB_1, ActiveStocksPage.Param.Type.UP_3, page, 20);
        } else {
            param.setPage(page);
        }

        SourceManager.getInstance().getActiveStocksPage(param.getParams(), new SourceManager.onSourceCallBack<ActiveStocksPage>() {

            public void onSuccess(String data) {
            }

            public void onFailed(String errorMsg) {
//                System.out.println(errorMsg);

                App.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingUtil.showReload(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingUtil.hideReload();
                                getActiveStocksPage(1, false);
                            }
                        });
                        recyclerView.refreshComplete();
                        recyclerView.loadMoreComplete();
                        recyclerView.setVisibility(View.GONE);
                        loadingUtil.hideLoading();
                        loadingUtil.hideNone();
                    }
                });

            }

            public DataType getType() {
                return DataType.bean;
            }

            public ActiveStocksPage getBean(Document document, String json) {
                ActiveStocksPage page = GsonUtil.getGson().fromJson(json, ActiveStocksPage.class);
                List<ActiveStocksPage.DataBean> dataBeanList = page.getData();
                //过滤删除的股票
                try {
                    String now = DateUtil.today(DateUtil.DEFAULT_DATETIME_FORMAT_SEC2);
                    List<DeleteStockBean> list = DeleteStockBean.find(DeleteStockBean.class, "time > ? and type = ?", now, SourceManager.StockType.ActiveStock.name());
                    if (list != null) {
                        for (DeleteStockBean bean : list) {
                            ActiveStocksPage.DataBean dataBean = new ActiveStocksPage.DataBean();
                            dataBean.setCode(bean.getStockCode());
                            if (dataBeanList != null && dataBeanList.contains(dataBean)) {
                                dataBeanList.remove(dataBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MessageEvent(GET_ACTIVE_STOCKS_PAGE_SUCCESS, page));
                return page;
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        StockUtil.getInstance().stopUpdateStockDetailTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activeStockAdapter != null) {
            StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, activeStockAdapter);
        }

    }

    @Override
    public void onEventMainThread(MessageEvent event) {
        super.onEventMainThread(event);
        switch (event.getType()) {
            case GET_ACTIVE_STOCKS_PAGE_SUCCESS:
                ActiveStocksPage page = (ActiveStocksPage) event.getArgs();
                if (page != null) {
                    if (activeStockAdapter == null) {
                        activeStockAdapter = new ActiveStockAdapter(getActivity(), page);
                        recyclerView.setAdapter(activeStockAdapter);
                        recyclerView.refreshComplete();
//                        activeStockAdapter.isRefreshing = false;
                        activeStockAdapter.setRefreshing(false);
                        StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, activeStockAdapter);
                        showMessage(String.format(SOURCE_DATE, page.getDate()));
                    } else {
                        if (param.getPage() == 1) {
                            activeStockAdapter.getActiveStocksPage().getData().clear();
                        }
                        activeStockAdapter.getActiveStocksPage().setTotal(page.getTotal());
                        activeStockAdapter.getActiveStocksPage().setPagesize(page.getPagesize());
                        activeStockAdapter.getActiveStocksPage().setPage(page.getPage());
                        activeStockAdapter.getActiveStocksPage().setCode(page.getCode());
                        activeStockAdapter.getActiveStocksPage().setDate(page.getDate());
                        if (page.getData().size() > 0) {
                            ActiveStocksPage.DataBean dataBean = page.getData().get(0);
                            if (!activeStockAdapter.getActiveStocksPage().getData().contains(dataBean)) {
                                activeStockAdapter.getActiveStocksPage().getData().addAll(page.getData());
                            } else {
                                LogUtil.d("异常........");
                            }
                        }
                    }
                    if (activeStockAdapter.getActiveStocksPage().getData().size() == 0) {
                        loadingUtil.showNone();
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        loadingUtil.hide();
                        if (recyclerView.getVisibility() != View.VISIBLE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                    if (activeStockAdapter.isLoading()) {
                        recyclerView.loadMoreComplete();
//                        activeStockAdapter.isLoading = false;
                        activeStockAdapter.setLoading(false);
                    } else if (activeStockAdapter.isRefreshing()) {
                        recyclerView.refreshComplete();
//                        activeStockAdapter.isRefreshing = false;
                        activeStockAdapter.setRefreshing(false);
                    }
                    activeStockAdapter.notifyDataSetChanged();

                }
                break;
            case UPDATE_STOCK_DETAIL:
                if (activeStockAdapter != null) {
                    int index = (int) event.getArgs();
                    // TODO: 16/9/6 这里用 notifyItemChanged 更新item的时候,会导致最后一个item无法更新,暂时的解决办法是index等于最后一个的时候就使用notifyDataSetChanged
                    if (index == activeStockAdapter.getActiveStocksPage().getData().size() - 1) {
                        activeStockAdapter.notifyDataSetChanged();
                    } else {
                        activeStockAdapter.notifyItemChanged(index);
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
                String code = event.getArgs().toString();
                if (activeStockAdapter != null && code != null) {
                    ActiveStocksPage.DataBean dataBean = new ActiveStocksPage.DataBean();
                    dataBean.setCode(code);
                    activeStockAdapter.getActiveStocksPage().getData().remove(dataBean);
                    activeStockAdapter.notifyDataSetChanged();
                }
                break;
            case AUTO_REFRESH_DATA:
                if (activeStockAdapter != null && !activeStockAdapter.isRefreshing()) {
//                    activeStockAdapter.isRefreshing = true;
                    activeStockAdapter.setRefreshing(true);
                    getActiveStocksPage(1, true);
                }

                break;
            case SHOW_POINTS_POOL_DIALOG:
                //积分不足,不能使用下拉刷新
                recyclerView.setPullRefreshEnabled(false);
                recyclerView.setLoadingMoreEnabled(false);
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
