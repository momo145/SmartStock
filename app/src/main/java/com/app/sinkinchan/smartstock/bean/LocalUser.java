package com.app.sinkinchan.smartstock.bean;

import com.sinkinchan.transport.module.UserBean;

import java.util.Date;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-10-22 下午8:31
 **/
public class LocalUser extends UserBean {
    //过时
    private Date OutOfDate;

    public LocalUser() {
    }

    public LocalUser(UserBean userBean) {
        if (userBean != null) {
            setId(userBean.getId());
            setAddTime(userBean.getAddTime());
            setUserName(userBean.getUserName());
            setIcon(userBean.getIcon());
            setGender(userBean.getGender());
            setThird_party_id(userBean.getThird_party_id());
            setPlatform(userBean.getPlatform());
            setIsOnline(userBean.getIsOnline());
            setLoginTime(userBean.getLoginTime());
            setLogOutTime(userBean.getLogOutTime());
        }
    }


    public Date getOutOfDate() {
        return OutOfDate;
    }

    public LocalUser setOutOfDate(Date outOfDate) {
        OutOfDate = outOfDate;
        return this;
    }
}
