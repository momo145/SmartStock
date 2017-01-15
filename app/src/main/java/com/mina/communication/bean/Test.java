package com.mina.communication.bean;

import java.io.Serializable;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-17 上午11:36
 **/
public class Test implements Serializable {
    private String title;
    private String message;

    public String getTitle() {
        return title;
    }

    public Test setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Test setMessage(String message) {
        this.message = message;
        return this;
    }
}
