package com.app.sinkinchan.smartstock.mode;

import android.databinding.ObservableField;

import com.sinkinchan.stock.sdk.bean.StockClassify;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-12-29 上午11:19
 **/
public class StockHandicapModeView {
    public ObservableField<StockHandicap> cap = new ObservableField<>(null);
    public ObservableField<StockClassify.Bean> classify = new ObservableField<>(null);


    public static class StockHandicap {
        public ObservableField<String> code = new ObservableField<>(null);
        public ObservableField<String> name = new ObservableField<>(null);
        public ObservableField<String> latestPrice = new ObservableField<>(null);
        public ObservableField<String> up = new ObservableField<>(null);
        public ObservableField<String> upOrDown = new ObservableField<>(null);
        public ObservableField<String> change = new ObservableField<>(null);
        public ObservableField<String> highest = new ObservableField<>(null);
        public ObservableField<String> minimum = new ObservableField<>(null);
        public ObservableField<String> dealQuantity = new ObservableField<>(null);
        public ObservableField<String> totalDealAmount = new ObservableField<>(null);
        public ObservableField<String> limitUp = new ObservableField<>(null);
        public ObservableField<String> limitDown = new ObservableField<>(null);
        public ObservableField<String> yesterdayPrice = new ObservableField<>(null);
        public ObservableField<String> currentNumber = new ObservableField<>(null);
        public ObservableField<String> outerDisc = new ObservableField<>(null);
        public ObservableField<String> insideDish = new ObservableField<>(null);
        public ObservableField<String> volumeRatioIndex = new ObservableField<>(null);
        public ObservableField<String> income = new ObservableField<>(null);
        public ObservableField<String> PERatiosDynamic = new ObservableField<>(null);
        public ObservableField<String> PERatios = new ObservableField<>(null);
        public ObservableField<String> netAssets = new ObservableField<>(null);
        public ObservableField<String> PBRatio = new ObservableField<>(null);
        public ObservableField<String> generalCapital = new ObservableField<>(null);
        public ObservableField<String> capitalization = new ObservableField<>(null);
        public ObservableField<String> circulationOfCapitalStock = new ObservableField<>(null);
        public ObservableField<String> circulationOfCapitalization = new ObservableField<>(null);
        public ObservableField<String> time = new ObservableField<>(null);
        public ObservableField<String> open = new ObservableField<>(null);
        public ObservableField<String> zllr = new ObservableField<>(null);
        public ObservableField<String> zllc = new ObservableField<>(null);
        public ObservableField<String> zljlr = new ObservableField<>(null);
        public ObservableField<String> cdd = new ObservableField<>(null);
        public ObservableField<String> dd = new ObservableField<>(null);
        public ObservableField<String> zd = new ObservableField<>(null);
        public ObservableField<String> xd = new ObservableField<>(null);
        public ObservableField<String> unit = new ObservableField<>(null);

    }
}
