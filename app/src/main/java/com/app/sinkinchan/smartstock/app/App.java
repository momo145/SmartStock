package com.app.sinkinchan.smartstock.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;

import com.app.sinkinchan.smartstock.constant.Constants;
import com.app.sinkinchan.smartstock.utils.Base;
import com.app.sinkinchan.smartstock.utils.http.Netroid;
import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-07-16 下午7:54
 **/
public class App extends SugarApp {
    private static App context;
    private boolean IS_DEBUG;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);
        context = (App) getApplicationContext();
        Base.initialize(context);
        IS_DEBUG = isApkDebugable(context);
        handler = new Handler(Looper.getMainLooper());
        Netroid.init(this);
        //创建app文件目录
        Constants.createDir();
        //初始化配置文件
        /*try {
            if (IS_DEBUG) {
                GlobalConfig.init(this, GlobalConfig.CONFIG_DEVELOP);
            } else {
                GlobalConfig.init(this, GlobalConfig.CONFIG_PRODUCTION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

    public boolean isDebug() {
        return IS_DEBUG;
    }

    public static App getInstance() {
        return context;
    }

    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     *
     * @param context
     * @return
     * @author SHANHY
     * @date 2015-8-7
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 主线程上运行
     *
     * @param runnable
     */
    public void runInUIThread(Runnable runnable) {
        handler.post(runnable);
    }

}
