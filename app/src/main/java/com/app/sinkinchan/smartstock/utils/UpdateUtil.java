package com.app.sinkinchan.smartstock.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.app.App;
import com.app.sinkinchan.smartstock.constant.Constants;
import com.app.sinkinchan.smartstock.utils.http.Netroid;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;

import static com.app.sinkinchan.smartstock.utils.ContextUtil.getApplicationContext;
import static com.app.sinkinchan.smartstock.utils.ContextUtil.startActivity;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-11-19 下午7:28
 **/
public class UpdateUtil {
    private Notification notif;
    private NotificationManager manager;
    private static UpdateUtil ourInstance = new UpdateUtil();

    public static UpdateUtil getInstance() {
        return ourInstance;
    }

    private UpdateUtil() {
    }

    /**
     * 显示下载提醒
     */
    public void showDownloadInNoticeBar(Context context) {
        manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.mipmap.app_icon;
        notif.tickerText = "正在更新";
        // 通知栏显示所用到的布局文件
        notif.contentView = new RemoteViews(context.getPackageName(),
                R.layout.notify_progress_bar);
        manager.notify(0, notif);
    }

    long lastRefreshTime;

    public void downFile(String apkUrl) {
        Netroid.addFileDownload(Constants.APP_UPDATE_URL.getAbsolutePath(), apkUrl, new Listener<Void>() {
            @Override
            public void onSuccess(Void response) {
                downFinish();
            }

            @Override
            public void onPreExecute() {
                super.onPreExecute();
                setDownloadStatus(DownloadStatus.downloading);
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                super.onProgressChange(fileSize, downloadedSize);
                myFileSize = fileSize;
                if (System.currentTimeMillis() - lastRefreshTime < 300) {
                    return;
                }
                lastRefreshTime = System.currentTimeMillis();
                updateProgressBar(downloadedSize, fileSize);
            }

            @Override
            public void onError(NetroidError error) {
                setDownloadStatus(DownloadStatus.failure);
                ToastUtil.showLongToast(getApplicationContext(), "更新失败");
            }
        });
    }

    private long myFileSize = 0;

    /**
     * 下载完成，增加通知栏的点击事件，点击时安装app
     */
    public void downFinish() {
        if (manager == null || notif == null) {
            return;
        }
        updateProgressBar(myFileSize, myFileSize);
        setDownloadStatus(DownloadStatus.success);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(Constants.APP_UPDATE_URL),
                "application/vnd.android.package-archive");
        PendingIntent pIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);
        notif.contentIntent = pIntent;
        //自动弹出安装
        startActivity(intent);
        manager.notify(0, notif);
    }

    /**
     * 更新下载进度
     */
    private void updateProgressBar(long downloadedSize, long fileSize) {
        if (manager == null || notif == null) {
            return;
        }
        String progressText = Formatter.formatFileSize(App.getInstance(), downloadedSize) + "/"
                + Formatter.formatFileSize(App.getInstance(), fileSize);
        notif.contentView.setTextViewText(R.id.tvProgress, progressText);
        notif.contentView.setProgressBar(R.id.content_view_progress, 100,
                (int) (downloadedSize * 100 / fileSize), false);
        manager.notify(0, notif);
    }

    /**
     * 设置下载状态
     */
    private void setDownloadStatus(DownloadStatus status) {
        if (manager == null || notif == null) {
            return;
        }
        int downloadTextId = R.string.app_is_downloading_text;
        int color = R.color.c8;
        switch (status) {
            case downloading:
                downloadTextId = R.string.app_is_downloading_text;
                break;
            case success:
                downloadTextId = R.string.app_download_success_text;
                break;
            case failure:
                color = R.color.c1;
                downloadTextId = R.string.app_download_failure_text;
                break;
        }
        notif.contentView.setTextViewText(R.id.tvContent, ResourceManager.getString(downloadTextId));
        notif.contentView.setTextColor(R.id.tvContent, ResourceManager.getResources().getColor(color));
        manager.notify(0, notif);
    }

    enum DownloadStatus {
        downloading,
        success,
        failure
    }
}
