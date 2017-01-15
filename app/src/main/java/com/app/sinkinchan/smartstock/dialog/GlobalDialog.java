package com.app.sinkinchan.smartstock.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.dialog.base.BaseDialog;
import com.app.sinkinchan.smartstock.event.MessageEvent;


/**
 * Created by sinkinchan on 2015/9/14.
 */
public class GlobalDialog extends BaseDialog implements View.OnClickListener {

    View view = null;
    TextView tv_title;
    TextView tv_message;
    Button btn_cancel;
    Button btn_confirm;
    boolean isShowCancelBtn;
    OnCancelClick onCancelClick;
    OnConfirmClick onConfirmClick;

    public GlobalDialog(Context context) {
        super(context, R.style.myDialog);
        view = LayoutInflater.from(context).inflate(R.layout.widget_global_dialog, null);
        setContentView(view);
        initView();
    }


    @Override
    protected void init() {
        super.init();
    }

    private void initView() {
        //获取控件
        if (view != null) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_message = (TextView) view.findViewById(R.id.tv_message);
            btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(this);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
            btn_confirm.setOnClickListener(this);

        }

    }

    @Override
    public void setTitle(CharSequence title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (tv_title != null) {
            tv_title.setText(titleId);
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void setMessage(CharSequence message) {
        if (tv_message != null) {
            tv_message.setText(message);
        }
    }

    public void setMessage(int message) {
        if (tv_message != null) {
            tv_message.setText(message);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (onCancelClick != null) {
                    onCancelClick.OnClick(v);
                }
                dismiss();
                break;
            case R.id.btn_confirm:
                if (onConfirmClick != null) {
                    onConfirmClick.OnClick(v);
                }
                dismiss();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView tv_title = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                dismiss();
                break;
        }
        return false;
    }


    @Override
    public void onEventMainThread(MessageEvent event) {

    }

    public interface OnCancelClick {
        void OnClick(View view);
    }

    public interface OnConfirmClick {
        void OnClick(View view);
    }

    public OnCancelClick getOnCancelClick() {
        return onCancelClick;
    }

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    public OnConfirmClick getOnConfirmClick() {
        return onConfirmClick;
    }

    public void setOnConfirmClick(OnConfirmClick onConfirmClick) {
        this.onConfirmClick = onConfirmClick;
    }

    public boolean isShowCancelBtn() {
        return isShowCancelBtn;
    }

    public void setShowCancelBtn(boolean showCancelBtn) {
        isShowCancelBtn = showCancelBtn;
    }

    public void setCancelBtnText(String str) {
        btn_cancel.setText(str);
    }

    public void setCancelBtnText(int str) {
        btn_cancel.setText(str);
    }

    public void setConfirmBtnText(String str) {
        btn_confirm.setText(str);
    }

    public void setConfirmBtnText(int str) {
        btn_confirm.setText(str);
    }

    /**
     * 使得标题完全去掉
     */
    public void setTitleGone() {
        tv_title.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        if (isShowCancelBtn) {
            btn_cancel.setVisibility(View.VISIBLE);
        } else {
            btn_cancel.setVisibility(View.INVISIBLE);
        }
        super.show();
    }
}
