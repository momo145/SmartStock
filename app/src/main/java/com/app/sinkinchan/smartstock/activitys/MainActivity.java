package com.app.sinkinchan.smartstock.activitys;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.bean.LocalUser;
import com.app.sinkinchan.smartstock.dialog.ActiveStockSettingDialog;
import com.app.sinkinchan.smartstock.dialog.ResearchStockSettingDialog;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.fragments.ActiveStockFragment;
import com.app.sinkinchan.smartstock.fragments.DeathSquadStockFragment;
import com.app.sinkinchan.smartstock.fragments.QuotaType1StockFragment;
import com.app.sinkinchan.smartstock.fragments.QuotaType2StockFragment;
import com.app.sinkinchan.smartstock.fragments.QuotaType3StockFragment;
import com.app.sinkinchan.smartstock.fragments.QuotaType4StockFragment;
import com.app.sinkinchan.smartstock.fragments.ResearchStockFragment;
import com.app.sinkinchan.smartstock.fragments.base.BaseFragment;
import com.app.sinkinchan.smartstock.glide.GlideImgManager;
import com.app.sinkinchan.smartstock.mina.MinaService;
import com.app.sinkinchan.smartstock.utils.APILevel;
import com.app.sinkinchan.smartstock.utils.DateUtil;
import com.app.sinkinchan.smartstock.utils.ResourceManager;
import com.app.sinkinchan.smartstock.utils.ToastUtil;
import com.app.sinkinchan.smartstock.utils.UserUtil;
import com.sinkinchan.stock.sdk.SourceManager;
import com.sinkinchan.stock.sdk.bean.QuotaStockPage;
import com.sinkinchan.stock.sdk.config.SdkConfig;
import com.sinkinchan.transport.module.UserBean;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStockType();
        initToolBar();
        initFB();
        initDrawerLayout();
        init();
        MinaService.getInstance().connect();
        initYouMiAD();
        /*checkAppVersion(false);
        checkInform();*/

    }

    @Override
    protected void onDestroy() {
//        OffersManager.getInstance(this).onAppExit();
        super.onDestroy();
    }

    TextView tv_stock_type;
    TextView tv_show_msg;
    ImageView btn_msg_show;
    FrameLayout fl_parent;

    private void initStockType() {
        tv_stock_type = (TextView) findViewById(R.id.tv_stock_type);
        tv_show_msg = (TextView) findViewById(R.id.tv_show_msg);
        btn_msg_show = (ImageView) findViewById(R.id.btn_msg_show);
        fl_parent = (FrameLayout) findViewById(R.id.fl_parent);
        btn_msg_show.setOnClickListener(v -> {
            showOrHideStockMsg();
        });
        tv_show_msg.setOnClickListener(v -> {
            showOrHideStockMsg();
        });

    }

    private void showOrHideStockMsg() {
        if (tv_stock_type == null || btn_msg_show == null) {
            return;
        }
        switch (tv_stock_type.getVisibility()) {
            case View.VISIBLE:
                tv_stock_type.setVisibility(View.GONE);
                tv_show_msg.setVisibility(View.VISIBLE);
                btn_msg_show.setImageResource(R.mipmap.icon_launch);
//                    fl_parent.setBackgroundColor(ResourceManager.getResources().getColor(R.color.other_bg));
                break;
            case View.GONE:
//                    fl_parent.setBackgroundColor(ResourceManager.getResources().getColor(R.color.transparent));
                tv_stock_type.setVisibility(View.VISIBLE);
                tv_show_msg.setVisibility(View.GONE);
                btn_msg_show.setImageResource(R.mipmap.icon_pack_up);
                break;
        }
    }

    /**
     * 检查通知
     */
    /*private void checkInform() {
        AdManager.getInstance(this).asyncGetOnlineConfig("inform_1_1_0", new OnlineConfigCallBack() {
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // TODO Auto-generated method stub
                // 获取在线参数成功
                if (!StringUtils.isBlank(value) && !StringUtils.equals(value, "0")) {
                    showInform(value);
                }

            }

            @Override
            public void onGetOnlineConfigFailed(String key) {
                // TODO Auto-generated method stub
                // 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
            }
        });
    }*/

    /*private void checkAppVersion(boolean isShowInfo) {
        if (isShowInfo) {
            showProgressDialog("正在检查更新");
        }
        AdManager.getInstance(this).asyncCheckAppUpdate(appUpdateInfo -> {
            // 检查更新回调，注意，这里是在 UI 线程回调的，因此您可以直接与 UI 交互，但不可以进行长时间的操作（如在这里访问网络是不允许的）
            if (appUpdateInfo == null || StringUtils.isBlank(appUpdateInfo.getUrl())) {
                // 当前已经是最新版本
                if (isShowInfo) {
                    ToastUtil.showLongToast(getApplicationContext(), "已经是最新版本了");
                }
                dismissProgressDialog();
            } else {
                // 有更新信息，开发者应该在这里实现下载新版本
                dismissProgressDialog();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提醒")
                        .setMessage("发现新版本,点击更新按钮进行更新.\n" + Html.fromHtml(appUpdateInfo.getUpdateTips()))
                        .setPositiveButton("更新", (dialogInfo, index) -> {
                            UpdateUtil.getInstance().showDownloadInNoticeBar(this);
                            UpdateUtil.getInstance().downFile(appUpdateInfo.getUrl());
                            dialogInfo.dismiss();
                        })
                        .show();
            }
        });
    }*/

    //    ProgressBar progressBar;
    //刷新时间为15秒
    private static final int REFRESH_TIME = 15000;
    Timer autoRefreshTimer = null;

    private void initProgressBar() {
//        progressBar = (ProgressBar) findViewById(progressBar);
//        startAutoRefreshTimer();
    }

    /**
     * 开始自动刷新定时器
     */
    private void startAutoRefreshTimer() {
//        if (progressBar != null) {
//            progressBar.setVisibility(View.VISIBLE);
//        }
        if (autoRefreshTimer != null) {
            autoRefreshTimer.cancel();
            autoRefreshTimer = null;
        }
        autoRefreshTimer = new Timer("自动刷新定时器");
        final long sleepTime = REFRESH_TIME / 100;
        autoRefreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.AUTO_REFRESH_DATA_COUNT_DOWN));
            }
        }, 0, sleepTime);
    }

    /**
     * 停止自动刷新定时器
     */
    private void stopAutoRefreshTimer() {
//        if (progressBar != null) {
//            progressBar.setVisibility(View.GONE);
//        }
        if (autoRefreshTimer != null) {
            autoRefreshTimer.cancel();
            autoRefreshTimer = null;
        }
    }

    private void initYouMiAD() {
       /* AdManager.getInstance(this).init("52b748be3c037799", "0dfcb12833bc5fcb", false, false);
        OffersManager.getInstance(this).onAppLaunch();*/
//        LocalUser user = UserUtil.getUser();
       /* PointsManager.getInstance(this).registerNotify(new PointsChangeNotify() {
            @Override
            public void onPointBalanceChange(float points) {
                if (user != null) {
                    if (tv_username != null) {
                        tv_username.setText(user.getUserName() + " 共有 " + points + " 积分");
                    }

                }

            }
        });*/
    }

    TabLayout tabLayout;

    private void initInstances() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        resetTabLayout();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BaseFragment.Type index = (BaseFragment.Type) tab.getTag();
                changePage(index);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ViewCompat.setElevation(tabLayout, 10);
    }

    protected void showMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT)
                .setAction("提醒", null).show();
    }

    private void resetTabLayout() {
        tabLayout.removeAllTabs();
        //tab1
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText(QuotaStockPage.Type.type1.getName());
        tab1.setTag(BaseFragment.Type.QuotaType1StockFragment);
        tabLayout.addTab(tab1, true);
        //tab2
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText(QuotaStockPage.Type.type2.getName());
        tab2.setTag(BaseFragment.Type.QuotaType2StockFragment);
        tabLayout.addTab(tab2);
        //tab3
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText(QuotaStockPage.Type.type3.getName());
        tab3.setTag(BaseFragment.Type.QuotaType3StockFragment);
        tabLayout.addTab(tab3);
        //tab4
        TabLayout.Tab tab4 = tabLayout.newTab();
        tab4.setText(QuotaStockPage.Type.type4.getName());
        tab4.setTag(BaseFragment.Type.QuotaType4StockFragment);
        tabLayout.addTab(tab4);
    }


    BaseFragment fragment = null;
    //如果处于当前fragment就不需要重新覆盖
    BaseFragment prevFragment = null;

    private void changePage(BaseFragment.Type type) {
        prevFragment = fragment;
        switch (type) {
            case ActiveStockFragment:
                fragment = new ActiveStockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.ActiveStock.title, SourceManager.StockType.ActiveStock.desc));
//                fragment = new LockFragment();
                break;
            case DeathSquadStockFragment:
                fragment = new DeathSquadStockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.DeathSquadStock.title, SourceManager.StockType.DeathSquadStock.desc));
                break;
            case ResearchStock:
                fragment = new ResearchStockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.ResearchStock.title, SourceManager.StockType.ResearchStock.desc));
                break;
            case QuotaType1StockFragment:
                fragment = new QuotaType1StockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.QuotaStockType1.title, SourceManager.StockType.QuotaStockType1.desc));
                break;
            case QuotaType2StockFragment:
                fragment = new QuotaType2StockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.QuotaStockType2.title, SourceManager.StockType.QuotaStockType2.desc));
                break;
            case QuotaType3StockFragment:
                fragment = new QuotaType3StockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.QuotaStockType3.title, SourceManager.StockType.QuotaStockType3.desc));
                break;
            case QuotaType4StockFragment:
                fragment = new QuotaType4StockFragment();
                tv_stock_type.setText(getString(R.string.stock_type_text, SourceManager.StockType.QuotaStockType4.title, SourceManager.StockType.QuotaStockType4.desc));
                break;
        }
        if (prevFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        } else if (!StringUtils.equals(prevFragment.getClass().getSimpleName(), fragment.getClass().getSimpleName())) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        tv_stock_type.setVisibility(View.VISIBLE);
        tv_show_msg.setVisibility(View.GONE);
        btn_msg_show.setImageResource(R.mipmap.icon_pack_up);
    }

    Toolbar toolbar = null;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    FloatingActionButton fab;

    private void initFB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (StockHandicapActivity.bitmap == null) {
                    activityScreenBitmap = ViewUtil.getScreenImage(getActivityView());
                }*/

                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.GO_TO_TOP));
//                ActiveStockSettingDialog.getInstance().show(MainActivity.this, ActiveStockFragment.param);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                changePage(BaseFragment.Type.DeathSquadStockFragment);*/
            }
        });

    }

    ImageView userIcon = null;

    private void initDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            tv_username = (TextView) headerView.findViewById(R.id.tv_username);
            tv_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*View view = getLayoutInflater().inflate(R.layout.activity_login, null);
                    view.findViewById(R.id.btn_wechat).setOnClickListener(onClickListener);
                    view.findViewById(R.id.btn_qq).setOnClickListener(onClickListener);
                    // 设置dialog没有title
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(view, new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                    Window window = dialog.getWindow();
                    // 可以在此设置显示动画
                    WindowManager.LayoutParams wl = window.getAttributes();
                    wl.x = 0;
                    wl.y = getWindowManager().getDefaultDisplay().getHeight();
                    // 以下这两句是为了保证按钮可以水平满屏
                    wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    // 设置显示位置
                    dialog.onWindowAttributesChanged(wl);
                    dialog.setTitle("快速登录");
                    dialog.show();*/
                }
            });
            userIcon = (ImageView) headerView.findViewById(R.id.user_icon);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    TextView tv_username;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void init() {
        SdkConfig.getInstance().setStockDetailThreadPoolSize(10).init();
        changePage(BaseFragment.Type.ActiveStockFragment);
        initInstances();
        initProgressBar();

    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    MenuItem settingItem = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (menu.size() >= 1) {
            settingItem = menu.getItem(0);
        }
        return true;
    }


    @Override
    public void onEventMainThread(MessageEvent event) {
        super.onEventMainThread(event);
        UserBean userBean = null;
        LocalUser localUser = null;
        switch (event.getType()) {
            case HIDE_SETTING:
                if (settingItem != null) {
                    settingItem.setVisible(false);
                }

                break;
            case SHOW_SETTING:
                if (settingItem != null) {
                    settingItem.setVisible(true);
                    if (fragment instanceof QuotaType1StockFragment) {
                        settingItem.setTitle("什么叫曙光初现?");
                    } else if (fragment instanceof QuotaType2StockFragment) {
                        settingItem.setTitle("什么叫空中加油?");
                    } else if (fragment instanceof QuotaType3StockFragment) {
                        settingItem.setTitle("什么叫超级短线?");
                    } else if (fragment instanceof QuotaType4StockFragment) {
                        settingItem.setTitle("什么叫蓄势待发?");
                    } else {
                        settingItem.setTitle("参数设置");
                    }
                }
                break;
            case HIDE_TAB_LAYOUT:
                if (tabLayout != null) {
                    tabLayout.setVisibility(View.GONE);
                }
                break;
            case SHOW_TAB_LAYOUT:
                if (tabLayout != null) {
                    resetTabLayout();
                    tabLayout.setVisibility(View.VISIBLE);
                }
                break;
            case SHOW_MESSAGE:
                String message = event.getArgs().toString();
                if (!StringUtils.isBlank(message)) {
                    showMessage(message);
                }
                break;
            case SET_TITLE:
                String title = event.getArgs().toString();
                if (!StringUtils.isBlank(title)) {
                    setTitle(title);
                }
                break;
            case REGISTER_SUCCESS:
//                ToastUtil.showLongToast(getApplicationContext(), "注册成功.");
                dismissProgressDialog();
                userBean = (UserBean) event.getArgs();
                String icon = userBean.getIcon();
                if (userBean != null) {
                    if (!StringUtils.isBlank(icon)) {
                        GlideImgManager.glideLoader(this, icon, userIcon, GlideImgManager.Type.Circle);
                    }
                    tv_username.setText(userBean.getUserName());
                }
                localUser = new LocalUser(userBean);
                //缓存本地用户
                try {
                    localUser.setOutOfDate(DateUtil.getNextMonth());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UserUtil.save(localUser);
                //注册成功,添加300积分初始
//                PointsManager.getInstance(this).awardPoints(300);
                break;
            case LOGIN_SUCCESS:
                ToastUtil.showLongToast(getApplicationContext(), "登录成功.");
                dismissProgressDialog();
                userBean = (UserBean) event.getArgs();
                if (userBean != null) {
//                    tv_username.setText(userBean.getUserName() + " 共有 " + userBean.getIntegral() + " 积分");
                    if (!StringUtils.isBlank(userBean.getIcon())) {
                        GlideImgManager.glideLoader(this, userBean.getIcon(), userIcon, GlideImgManager.Type.Circle);
                    }
                    localUser = new LocalUser(userBean);
                    //缓存本地用户
                    try {
                        localUser.setOutOfDate(DateUtil.getNextMonth());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    UserUtil.save(localUser);
                }
//                float points = PointsManager.getInstance(this).queryPoints();
                /*if (points > 0) {
                    tv_username.setText(userBean.getUserName() + " 共有 " + points + " 积分");
                }*/
                break;
            case STOCK_HANDICAP:
                final CardView cardView = (CardView) event.getArgs();
                StockHandicapActivity.cardView = cardView;
                Intent intent = new Intent(getApplicationContext(), StockHandicapActivity.class);
                if (APILevel.requireLollipop()) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                    if (cardView != null) {
                        options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, cardView, "cardView");
                    }
                    startActivity(intent, options.toBundle());

                } else {
                    startActivity(intent);
                }
                break;
            case AUTO_REFRESH_DATA_COUNT_DOWN:
//                int progress = progressBar.getProgress();
//                if (progress >= 100) {
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.AUTO_REFRESH_DATA));
//                    progress = 0;
//                }
//                progress += 1;
//                progressBar.setProgress(progress);
                break;
        }
    }

    boolean isShowInfo = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String title = "";
        String message = "";

        switch (id) {
            case R.id.action_setting:
                isShowInfo = false;
                if (fragment != null) {
                    if (fragment instanceof ActiveStockFragment) {
                        ActiveStockSettingDialog.getInstance().show(MainActivity.this, ActiveStockFragment.param);
                    } else if (fragment instanceof ResearchStockFragment) {
                        ResearchStockSettingDialog.getInstance().show(MainActivity.this, ResearchStockFragment.param);
                    } else if (fragment instanceof QuotaType1StockFragment) {
                        isShowInfo = true;
                        title = ResourceManager.getString(R.string.sgcx_title_text);
                        message = ResourceManager.getString(R.string.sgcx_message_text);
                    } else if (fragment instanceof QuotaType2StockFragment) {
                        isShowInfo = true;
                        title = ResourceManager.getString(R.string.kzjy_title_text);
                        message = ResourceManager.getString(R.string.kzjy_message_text);
                    }
                    if (isShowInfo) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle(title).setMessage(message).setPositiveButton("确定", null);
                        dialog.show();
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_active_stock:
                if (!(fragment instanceof ActiveStockFragment)) {
                    changePage(BaseFragment.Type.ActiveStockFragment);
                }
                break;
            case R.id.nav_research_stock:
                if (!(fragment instanceof ResearchStockFragment)) {
                    changePage(BaseFragment.Type.ResearchStock);
                }
                break;
            case R.id.nav_death_stock:
                if (!(fragment instanceof DeathSquadStockFragment)) {
                    changePage(BaseFragment.Type.DeathSquadStockFragment);
                }
                break;
            case R.id.nav_quota_stock:
                if (!(fragment instanceof QuotaType1StockFragment)) {
                    changePage(BaseFragment.Type.QuotaType1StockFragment);
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SHOW_TAB_LAYOUT));
                break;
            case R.id.nav_share:
//                OffersManager.getInstance(MainActivity.this).showOffersWall();
                break;
            case R.id.nav_send:
                Intent intent = new Intent(BaseActivity.currentActivity, FeedbackActivity.class);
                BaseActivity.currentActivity.startActivity(intent);
                break;
            case R.id.nav_future:
                /*showProgressDialog("获取中");
                // 方法二： 异步调用（可在任意线程中调用）
                AdManager.getInstance(this).asyncGetOnlineConfig("coming", new OnlineConfigCallBack() {
                    @Override
                    public void onGetOnlineConfigSuccessful(String key, String value) {
                        // TODO Auto-generated method stub
                        // 获取在线参数成功
                        dismissProgressDialog();
                        showFutureFunctionMsg(value);
                    }

                    @Override
                    public void onGetOnlineConfigFailed(String key) {
                        // TODO Auto-generated method stub
                        // 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
                        dismissProgressDialog();
                        ToastUtil.showLongToast(getApplicationContext(), "获取失败");
                    }
                });*/
                break;
            case R.id.nav_update:
//                checkAppVersion(true);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 显示将要添加的功能
     */
    private void showFutureFunctionMsg(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("即将到来的功能")
                .setMessage(Html.fromHtml(msg))
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 显示通知
     */
    private void showInform(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("通知")
                .setMessage(Html.fromHtml(msg))
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 第三方登录
     */
 /*   private void ThirdLogin(String name) {
        showProgressDialog("");
        Platform platform = ShareSDK.getPlatform(getApplicationContext(), name);
        *//*if (platform.isValid()) {
            platform.removeAccount();
        }*//*
        platform.setPlatformActionListener(this);
        if (platform.isClientValid()) {
            platform.SSOSetting(false);
            platform.authorize();
        } else {
            platform.showUser(null);
        }


//        wechat.authorize();
    }*/


   /* @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        LogUtil.d("onComplete=" + hashMap);
        UserBean userBean = new UserBean();
        userBean.setGender(platform.getDb().getUserGender());
        userBean.setUserName(platform.getDb().getUserName());
        userBean.setIcon(platform.getDb().getUserIcon());
        userBean.setThird_party_id(platform.getDb().getUserId());
        userBean.setPlatform(platform.getName());
        userBean.setType(TransportType.register);
        MinaService.getInstance().write(userBean, null);
    }
*/
   /* @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LogUtil.d("onError=" + throwable.getLocalizedMessage());
        dismissProgressDialog();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.d("onCancel=" + platform.getName());
        dismissProgressDialog();
    }*/
    @Override
    protected void createListener() {
        super.createListener();
//        ShareSDK.initSDK(this);
    }

    @Override
    protected void destroyListener() {
        super.destroyListener();
//        ShareSDK.stopSDK(this);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showLongToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
