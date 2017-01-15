package com.app.sinkinchan.smartstock.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-10-22 下午8:13
 **/
public class GlideImgManager {
    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }

    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param type
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, Type type) {
        switch (type) {
            case Circle:
                Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
                break;
            case Normal:
                Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
                break;
        }
    }

    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param iv
     * @param type
     */
    public static void glideLoader(Context context, String url, ImageView iv, Type type) {
        switch (type) {
            case Circle:
                Glide.with(context).load(url).transform(new GlideCircleTransform(context)).into(iv);
                break;
            case Normal:
                Glide.with(context).load(url).into(iv);
                break;
        }
    }

    public enum Type {
        Circle, Normal
    }
}
