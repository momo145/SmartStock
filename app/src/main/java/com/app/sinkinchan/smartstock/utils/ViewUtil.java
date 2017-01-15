package com.app.sinkinchan.smartstock.utils;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.view.View;

import com.app.sinkinchan.smartstock.app.App;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-21 下午10:26
 **/
public class ViewUtil {
    public static void setVisibility(int index, @NonNull View... views) {
        for (View v : views) {
            v.setVisibility(index);
        }
    }

    /**
     * 获取截图
     *
     * @param view
     * @return
     */
    public static Bitmap getScreenImage(View view) { // 截取一张屏幕的图片
//        view.setBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getWidth(), view
                .getHeight());
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 高斯模糊
     *
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap blur(Bitmap bitmap, float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(App.getInstance()); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); //
        // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 开辟输入内存
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 开辟输出内存
        gaussianBlue.setRadius(radius); // 设置模糊半径，范围0f<radius<=25f
        gaussianBlue.setInput(allIn); // 设置输入内存
        gaussianBlue.forEach(allOut); // 模糊编码，并将内存填入输出内存
        allOut.copyTo(output); // 将输出内存编码为Bitmap，图片大小必须注意
        rs.destroy(); // 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
        return output;
    }
}

