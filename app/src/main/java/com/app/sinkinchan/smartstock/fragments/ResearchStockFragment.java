package com.app.sinkinchan.smartstock.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.adapters.ResearchStockAdapter;
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
import com.sinkinchan.stock.sdk.bean.ResearchStockPage;
import com.sinkinchan.stock.sdk.utils.GsonUtil;
import com.sinkinchan.transport.module.TransportType;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.GET_RESEARCH_STOCKS_PAGE_SUCCESS;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午4:44
 **/
public class ResearchStockFragment extends BaseFragment {
    XRecyclerView recyclerView;
    LoadingUtil loadingUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        loadingUtil = LoadingUtil.newInstance(view);
        initRecycleView(view);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_SETTING));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HIDE_TAB_LAYOUT));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SET_TITLE, SourceManager.StockType.ResearchStock.title));
        askUseFunction(TransportType.useFunction_ResearchStock.name());
        return view;
    }


    LinearLayoutManager linearLayoutManager = null;
    //    ActiveStockAdapter activeStockAdapter = null;
    ResearchStockAdapter adapter = null;

    /**
     * 初始化RecycleView
     */
    boolean isLoadMore = false;

    private void initRecycleView(View view) {
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        View headerView = StockUtil.getInstance().getHeaderView(view.getContext(), SourceManager.StockType.ResearchStock);
//        //设置Item增加、移除动画
//        recyclerView.addHeaderView(headerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScaleParty);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (adapter != null) {
                    adapter.isRefreshing = true;
                }
                getResearchStocksPage(1, true);

            }

            @Override
            public void onLoadMore() {
                if (adapter != null && adapter.getResearchStockPage() != null && param != null) {
                    int total = adapter.getResearchStockPage().getTotal();
                    int pageSize = adapter.getResearchStockPage().getPagesize();
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
                            adapter.isLoading = true;
                        }
                        getResearchStocksPage(pageNum, true);
                    }
                }

            }
        });
        getResearchStocksPage(1, false);
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

    public static ResearchStockPage.Param param = null;

    //获取活跃股
    private void getResearchStocksPage(int page, boolean isInitiativeRefresh) {
        if (!isInitiativeRefresh) {
            loadingUtil.showLoading();
        }
        if (param == null) {
            param = new ResearchStockPage.Param(1, 30, ResearchStockPage.Type.five);
        } else {
            param.setPage(page);
        }

        SourceManager.getInstance().getActiveStocksPage(param.getParams(), new SourceManager.onSourceCallBack<ResearchStockPage>() {

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
                                getResearchStocksPage(1, false);
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

            public ResearchStockPage getBean(Document document, String json) {
                ResearchStockPage page = GsonUtil.getGson().fromJson(json, ResearchStockPage.class);
                List<ResearchStockPage.DataBean> dataBeanList = page.getData();
                //过滤删除的股票
                try {
                    String now = DateUtil.today(DateUtil.DEFAULT_DATETIME_FORMAT_SEC2);
                    List<DeleteStockBean> list = DeleteStockBean.find(DeleteStockBean.class, "time > ? and type = ?", now, SourceManager.StockType.ResearchStock.name());
                    if (list != null) {
                        for (DeleteStockBean bean : list) {
                            ResearchStockPage.DataBean dataBean = new ResearchStockPage.DataBean();
                            dataBean.setCode(bean.getStockCode());
                            if (dataBeanList != null && dataBeanList.contains(dataBean)) {
                                dataBeanList.remove(dataBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MessageEvent(GET_RESEARCH_STOCKS_PAGE_SUCCESS, page));
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
        if (adapter != null) {
            StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, adapter);
        }

    }

    @Override
    public void onEventMainThread(MessageEvent event) {
        super.onEventMainThread(event);
        switch (event.getType()) {
            case GET_RESEARCH_STOCKS_PAGE_SUCCESS:
                ResearchStockPage page = (ResearchStockPage) event.getArgs();
                if (page != null) {
                    if (adapter == null) {
                        adapter = new ResearchStockAdapter(getActivity(), page);
                        recyclerView.setAdapter(adapter);
                        recyclerView.refreshComplete();
                        adapter.isRefreshing = false;
                        StockUtil.getInstance().startUpdateStockDetailTimer(recyclerView, linearLayoutManager, adapter);
                        showMessage(String.format(SOURCE_DATE, page.getDate()));
                    } else {
                        if (param.getPage() == 1) {
                            adapter.getResearchStockPage().getData().clear();
                        }
                        adapter.getResearchStockPage().setTotal(page.getTotal());
                        adapter.getResearchStockPage().setPagesize(page.getPagesize());
                        adapter.getResearchStockPage().setPage(page.getPage());
                        adapter.getResearchStockPage().setCode(page.getCode());
                        adapter.getResearchStockPage().setDate(page.getDate());
                        if (page.getData().size() > 0) {
                            ResearchStockPage.DataBean dataBean = page.getData().get(0);
                            if (!adapter.getResearchStockPage().getData().contains(dataBean)) {
                                adapter.getResearchStockPage().getData().addAll(page.getData());
                            } else {
                                LogUtil.d("异常........");
                            }
                        }
                    }
                    if (adapter.getResearchStockPage().getData().size() == 0) {
                        loadingUtil.showNone();
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        loadingUtil.hide();
                        if (recyclerView.getVisibility() != View.VISIBLE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                    if (adapter.isLoading) {
                        recyclerView.loadMoreComplete();
                        adapter.isLoading = false;
                    } else if (adapter.isRefreshing) {
                        recyclerView.refreshComplete();
                        adapter.isRefreshing = false;
                    }
                    adapter.notifyDataSetChanged();

                }
                break;
            case UPDATE_STOCK_DETAIL:
                if (adapter != null) {
                    int index = (int) event.getArgs();
                    // TODO: 16/9/6 这里用 notifyItemChanged 更新item的时候,会导致最后一个item无法更新,暂时的解决办法是index等于最后一个的时候就使用notifyDataSetChanged
                    if (index == adapter.getResearchStockPage().getData().size() - 1) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemChanged(index);
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
