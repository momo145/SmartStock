package com.app.sinkinchan.smartstock.mina.modle;

import java.io.Serializable;

/**
 * 传输控制模块
 *
 * @author 陈欣健 2014年8月9日下午11:01:18
 *
 */
public class Module implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 5271977975374599931L;
	/**
	 * 控制代码
	 */
	private  int code;
	/**
	 * 返回的json
	 */
	private String json;

	public Module(int code, String json) {
		super();
		this.code = code;
		this.json = json;
	}

	public Module() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String toString() {
		return "Module [code=" + code + ", json=" + json + "]";
	}


}
