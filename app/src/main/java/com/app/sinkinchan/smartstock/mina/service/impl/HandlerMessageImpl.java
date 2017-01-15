package com.app.sinkinchan.smartstock.mina.service.impl;

import com.app.sinkinchan.smartstock.event.MessageEvent;
import com.app.sinkinchan.smartstock.mina.modle.Module;
import com.app.sinkinchan.smartstock.mina.service.HandlerMessage;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.sinkinchan.transport.module.ActionResponse;
import com.sinkinchan.transport.module.TransportType;
import com.sinkinchan.transport.module.UserBean;

import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;

import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.LOGIN_SUCCESS;
import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.REGISTER_SUCCESS;
import static com.app.sinkinchan.smartstock.event.MessageEvent.Type.USE_FUNCTION;

public class HandlerMessageImpl implements HandlerMessage {

    @Override
    public void handlerMessage(IoSession session, Object module) {
        if (module instanceof UserBean) {
            UserBean userBean = (UserBean) module;
            TransportType type = userBean.getType();
            switch (type) {
                case registerSuccess:
                    EventBus.getDefault().post(new MessageEvent(REGISTER_SUCCESS, userBean));
                    break;
                case loginSuccess:
                    EventBus.getDefault().post(new MessageEvent(LOGIN_SUCCESS, userBean));
                    break;
            }

        } else if (module instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) module;
            switch (actionResponse.getType()) {
                case useFunction:
                    EventBus.getDefault().post(new MessageEvent(USE_FUNCTION, actionResponse));
                    break;
            }
        }
    }


    @Override
    public void getVersion(IoSession session, Module module) {
        String json = module.getJson();
        LogUtil.d("json=" + json);
    }
}
