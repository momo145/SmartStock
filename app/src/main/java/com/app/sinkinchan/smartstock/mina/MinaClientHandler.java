package com.app.sinkinchan.smartstock.mina;

import com.app.sinkinchan.smartstock.mina.service.HandlerMessage;
import com.app.sinkinchan.smartstock.mina.service.impl.HandlerMessageImpl;
import com.app.sinkinchan.smartstock.utils.DeviceUtils;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.sinkinchan.transport.module.TransportType;
import com.sinkinchan.transport.module.UserBean;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;


public class MinaClientHandler extends IoHandlerAdapter {
    // 当客户端连接进入时
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // System.out.println("incomming 客户端: " + session.getRemoteAddress());
        // session.write("i am coming");
        // 自动登录
        oneKeyLogin();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        LogUtil.d(cause.getMessage());
        System.out.println("客户端发送信息异常....");
    }

    private void oneKeyLogin() {
        String key = DeviceUtils.getUniquePsuedoID();
        UserBean userBean = new UserBean();
        userBean.setGender("m");
        userBean.setUserName("user_" + DeviceUtils.getUniqueID());
        userBean.setIcon("");
        userBean.setThird_party_id(key);
        userBean.setPlatform("oneKeyLogin");
        userBean.setType(TransportType.register);
        MinaService.getInstance().write(userBean, null);
    }

    // 当客户端发送消息到达时
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        HandlerMessage handlerMessage = new HandlerMessageImpl();
        handlerMessage.handlerMessage(session, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // System.out.println("客户端与服务端断开连接.....");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        // System.out
        // .println("one Client Connection" + session.getRemoteAddress());

    }

}
