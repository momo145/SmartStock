package com.app.sinkinchan.smartstock.mina.modle;

import java.io.Serializable;

public class MinaMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String message;
	public static final int SUCCESS = 1000;
	public static final int ERROR = 1001;

	public MinaMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MinaMessage(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
