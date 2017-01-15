package com.app.sinkinchan.smartstock.mina.service;

import com.app.sinkinchan.smartstock.mina.modle.Module;

import org.apache.mina.core.session.IoSession;


public interface HandlerMessage {

    void handlerMessage(IoSession session, Object module);


    /**
     * 获取版本号
     *
     * @param session
     * @param module
     */
    void getVersion(IoSession session, Module module);
}
