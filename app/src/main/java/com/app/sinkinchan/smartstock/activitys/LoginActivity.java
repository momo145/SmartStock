package com.app.sinkinchan.smartstock.activitys;

import android.os.Bundle;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;

public class LoginActivity extends BaseActivity {

//    TextView btn_wechat, btn_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void init() {
//        btn_wechat = (TextView) findViewById(btn_wechat);
//        btn_qq = (TextView) findViewById(btn_qq);
//        btn_wechat.setOnClickListener(onClickListener);
//        btn_qq.setOnClickListener(onClickListener);
    }

    /*View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case btn_wechat:
                    ThirdLogin(Wechat.NAME);
                    break;
                case btn_qq:
                    ThirdLogin(QQ.NAME);
                    break;
            }
        }
    };*/

/*
    *//**
     * 第三方登录
     *//*
    private void ThirdLogin(String name) {
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
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        LogUtil.d("onComplete=" + hashMap);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LogUtil.d("onError=" + throwable.getLocalizedMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.d("onCancel=" + platform.getName());
    }

    @Override
    protected void createListener() {
        super.createListener();
        ShareSDK.initSDK(this);
    }

    @Override
    protected void destroyListener() {
        super.destroyListener();
        ShareSDK.stopSDK(this);
    }*/

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }
}
