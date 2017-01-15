package com.app.sinkinchan.smartstock.mina;

import com.app.sinkinchan.smartstock.mina.constant.MinaConstant;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.NetUtils;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MinaClient {
	/**
	 * 连接到服务器
	 */
	private ConnectFuture cf;
	private static MinaClient minaClient = new MinaClient();
	private MinaClient() {
	}

	public static MinaClient getInstance() {
		return minaClient;
	}
	public void connect() {
		// Create TCP/IP connection
		final NioSocketConnector connector = new NioSocketConnector();
		// 创建接受数据的过滤器
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		// 设定这个过滤器将一行一行(/r/n)的读取数据
		chain.addLast("myChin", new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory()));

		// 客户端的消息处理器：一个SamplMinaServerHander对象
		connector.setHandler(new MinaClientHandler());

		// set connect timeout
		connector.setConnectTimeoutMillis(30000);// 设置连接超时
		// 断线重连回调拦截器
		connector.getFilterChain().addFirst("reconnection",
				new IoFilterAdapter() {
					@Override
					public void sessionClosed(NextFilter nextFilter,
											  IoSession ioSession) throws Exception {
						for (;;) {
							try {
								Thread.sleep(3000);
								cf = connector.connect();
								cf.awaitUninterruptibly();// 等待连接创建成功
								// session = cf.getSession();// 获取会话
								if (cf.getSession().isConnected()) {
									LogUtil.d("断线重连成功");
									break;
								}
							} catch (Exception ex) {
								// LogUtil.e("重连服务器登录失败,3秒再连接一次:"
								// + ex.getMessage());
							}
						}
					}
				});
		// 连接到服务器：
		if (NetUtils.isConnected()) {
			connector.setDefaultRemoteAddress(new InetSocketAddress(
					MinaConstant.SERVER_IP_ADDRESS, MinaConstant.SERVER_POST));
		} else {
			connector.setDefaultRemoteAddress(new InetSocketAddress(
					"192.168.1.136", MinaConstant.SERVER_POST));
		}

		for (;;) {
			try {
				cf = connector.connect();
				// 等待连接创建成功
				cf.awaitUninterruptibly();
				CloseFuture closeFuture = cf.getSession().getCloseFuture();
				if (closeFuture != null) {
					closeFuture.awaitUninterruptibly();
				}

				// connector.dispose();
				// 获取会话
//				session = future.getSession();
				 LogUtil.d("连接服务端"
				 + "[成功]"
				 + ",,时间:"
				 + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				 .format(new Date()));
				break;
			} catch (Throwable e) {
				 LogUtil.e("连接服务端"
				 + "失败"
				 + "..时间:"
				 + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				 .format(new Date())
				 + ", 连接MSG异常,请检查MSG端口、IP是否正确,MSG服务是否启动,异常内容:"
				 + e.getMessage());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}// 连接失败后,重连间隔5s
			}
		}
		// cf = connector.connect();
		//
		// // Wait for the connection attempt to be finished.
		// cf.awaitUninterruptibly();
		// if (cf.isConnected()) {
		// LogUtil.d("连接成功..");
		// cf.getSession().getCloseFuture().awaitUninterruptibly();
		// connector.dispose();
		// } else {
		// cf = null;
		// LogUtil.e("连接失败..");
		// }

	}

	/**
	 * 获得session
	 *
	 * @return
	 */
	public IoSession getSession() {
		if (cf == null) {
			return null;
		}
		if (cf.isConnected()) {
			return cf.getSession();
		}
		return null;
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
	}
}
