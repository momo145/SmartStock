package com.app.sinkinchan.smartstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.fragments.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午4:44
 **/
public class LockFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock, container, false);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HIDE_SETTING));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.HIDE_TAB_LAYOUT));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SET_TITLE, "解锁功能"));
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
