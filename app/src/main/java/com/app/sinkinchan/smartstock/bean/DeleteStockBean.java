package com.app.sinkinchan.smartstock.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-19 下午10:05
 **/
@Table
public class DeleteStockBean extends SugarRecord {
    private String stockCode;
    private String time;
    private String type;

    public String getStockCode() {
        return stockCode;
    }

    public DeleteStockBean setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public String getTime() {
        return time;
    }

    public DeleteStockBean setTime(String time) {
        this.time = time;
        return this;
    }

    public String getType() {
        return type;
    }

    public DeleteStockBean setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteStockBean{" +
                "stockCode='" + stockCode + '\'' +
                ", time=" + time +
                ", type=" + type +
                '}';
    }
}
