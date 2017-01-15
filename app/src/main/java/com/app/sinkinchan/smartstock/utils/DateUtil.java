package com.app.sinkinchan.smartstock.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-16 下午8:43
 **/
public class DateUtil extends com.sinkinchan.stock.sdk.utils.DateUtil {

    /**
     *
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @return
     */
    public static int getSeason() {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(getCurDate());
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }
    private static final String Time_0930 = " 09:30:00";
    private static final String Time_2359 = " 23:59:59";

    public static String getDeleteExpiredTime() throws Exception {
        Date now = getCurDate(DEFAULT_DATETIME_FORMAT_SEC2);
        String today = toString(new Date(), DEFAULT_DATE_FORMAT);
        String today_0930 = today + Time_0930;
        Date date_0903 = parseDate(today_0930, DEFAULT_DATETIME_FORMAT_SEC2);
        Date date_2359 = parseDate(today + Time_2359, DEFAULT_DATETIME_FORMAT_SEC2);
        if (date_2359.getTime() > now.getTime() && now.getTime() < date_0903.getTime()) {
            LogUtil.d("现在的时间在在23:59到09:30之间");
            //判断现在的时间在23:59到09:30的话,过期时间就是当天的9.30
            return today_0930;
        } else {
            LogUtil.d("现在的时间在在09:30到23:59之间");
            //判断现在的时间在09:30到23:59之间的话,过期时间就是明天的9.30
            return getTomorrow() + Time_0930;
        }
    }

    public static String getTomorrow() throws Exception {
        //获取当前日期
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String nowDate = sf.format(date);
        System.out.println(nowDate);
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(sf.parse(nowDate));
        cal.add(Calendar.DAY_OF_YEAR, +1);
        String nextDate_1 = sf.format(cal.getTime());
        return nextDate_1;
    }

    /**
     * 获取下月
     *
     * @return
     * @throws Exception
     */
    public static Date getNextMonth() throws Exception {
        //获取当前日期
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String nowDate = sf.format(date);
        System.out.println(nowDate);
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(sf.parse(nowDate));
        cal.add(Calendar.DAY_OF_YEAR, +1);
        return cal.getTime();
    }

}
