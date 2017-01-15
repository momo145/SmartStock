package com.app.sinkinchan.smartstock.mina;

import com.app.sinkinchan.smartstock.utils.LogUtil;

import org.apache.mina.core.session.IoSession;

public class MinaService {


    private static MinaService minaService = new MinaService();

    private MinaService() {
    }

    public static MinaService getInstance() {
        return minaService;
    }

    /**
     * 连接到服务器
     */
    public void connect() {
        IoSession session = MinaClient.getInstance().getSession();
        if (session == null || !session.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MinaClient.getInstance().connect();
                }
            }).start();
        }

    }

    /**
     * 写出
     *
     * @param module
     */
    public void write(Object module, MessageCallBack callBack) {
        IoSession session = MinaClient.getInstance().getSession();
        if (session != null && session.isConnected()) {
            session.write(module);
            if (callBack != null) {
                callBack.success();
            }
            LogUtil.d("发送成功=" + session.getId());
        } else {
            if (callBack != null) {
                callBack.failed();
            }
            LogUtil.e("未连上服务器,发送失败...");
        }
    }

    public interface MessageCallBack {
        void success();

        void failed();
    }
}
