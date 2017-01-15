package com.app.sinkinchan.smartstock.event;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-07-16 下午11:40
 **/
public class MessageEvent {
    public enum Type {
        GET_ACTIVE_STOCKS_PAGE_SUCCESS, UPDATE_STOCK_DETAIL, REFRESH_STOCK, GET_STOCK_HISTORY_PRICE_SUCCESS,
        GET_STOCK_NEWS_SUCCESS, GET_STOCK_NOTICES_SUCCESS, GET_RESEARCH_STOCKS_PAGE_SUCCESS, GO_TO_TOP,
        OPEN_SETTING, HIDE_SETTING, SHOW_SETTING, GET_DEATH_SQUAD_STOCK_SUCCESS, GET_QUOTA_STOCK_SUCCESS,
        HIDE_TAB_LAYOUT, SHOW_TAB_LAYOUT, SHOW_MESSAGE, SET_TITLE, DELETE_ITEM, GET_STOCK_QUOTA_SUCCESS, REGISTER_SUCCESS,
        LOGIN_SUCCESS, STOCK_HANDICAP, AUTO_REFRESH_DATA_COUNT_DOWN, AUTO_REFRESH_DATA, REWARD_POINTS, SHOW_POINTS_POOL_DIALOG,
        USE_FUNCTION,STOCK_HANDICAP_MORE;
    }

    private Type type;
    private Object args;

    public MessageEvent(Type type) {
        this.type = type;
    }

    public MessageEvent(Type type, Object args) {
        this.type = type;
        this.args = args;
    }

    public Type getType() {
        return type;
    }

    public Object getArgs() {
        return args;
    }


}
