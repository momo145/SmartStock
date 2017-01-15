package com.app.sinkinchan.smartstock.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.dialog.base.BaseDialog;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-04 下午10:49
 **/
public class LoadMoreDialog extends BaseDialog {
    public LoadMoreDialog(Context context) {
        super(context, R.style.CommonDialog);
//        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load_more);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
