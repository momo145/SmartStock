package com.app.sinkinchan.smartstock.activitys;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.constant.Constants;
import com.app.sinkinchan.smartstock.utils.LoadingUtil;
import com.app.sinkinchan.smartstock.utils.LogUtil;
import com.app.sinkinchan.smartstock.utils.ToastUtil;
import com.app.sinkinchan.smartstock.utils.http.Netroid;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

import org.jsoup.helper.StringUtil;

import java.io.File;


public class PDFViewerActivity extends BaseActivity {

    private PDFView pdfView;
    private String url, title;
    private File pdfFile = null;
    LoadingUtil loadingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        loadingUtil = LoadingUtil.newInstance(this);
        init();
    }


    @Override
    protected void init() {
        url = getIntent().getStringExtra(WEB_VIEW_LOAD_URL);
        title = getIntent().getStringExtra(WEB_VIEW_TITLE);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        setBackMenu();
        setTitle(title);
        if (StringUtil.isBlank(url) || StringUtil.isBlank(title)) {
            ToastUtil.showLongToast(getApplicationContext(), "加载公告异常..");
            return;
        }
        loadOrDownloadPdf();

    }

    private void loadOrDownloadPdf() {
        pdfFile = new File(Constants.APP_FILE_PDF_CACHE_DIR, title + ".pdf");
        if (pdfFile.exists()) {
            load(pdfFile);
        } else {
            //下载pdf
            downPdf();
        }
    }

    private long lastRefreshTime = 0;

    /**
     * download pdf file.
     */
    private void downPdf() {
        Netroid.addFileDownload(pdfFile.getAbsolutePath(), url, new Listener<Void>() {
            @Override
            public void onSuccess(Void response) {
                LogUtil.d("下载成功");
                load(pdfFile);
            }

            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                super.onProgressChange(fileSize, downloadedSize);
                if (System.currentTimeMillis() - lastRefreshTime < 300) {
                    return;
                }
                lastRefreshTime = System.currentTimeMillis();
                LogUtil.d("downloadedSize=" + downloadedSize + " fileSize=" + fileSize);
            }

            @Override
            public void onError(NetroidError error) {
                LogUtil.d("下载失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingUtil.showReload(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingUtil.showLoading();
                                loadOrDownloadPdf();
                            }
                        });
                    }
                });
            }
        });
    }

    private void load(File file) {
        if (file != null && pdfFile.exists()) {
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .onDraw(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            LogUtil.d("加载成功");
                            loadingUtil.hide();
                        }
                    })
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {

                        }
                    })
                    .onPageScroll(new OnPageScrollListener() {
                        @Override
                        public void onPageScrolled(int page, float positionOffset) {

                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                            LogUtil.d("异常了.....");
                        }
                    })
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .load();
        }
    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }


}
