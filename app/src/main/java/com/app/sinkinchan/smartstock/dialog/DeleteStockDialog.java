package com.app.sinkinchan.smartstock.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-19 下午9:16
 **/
public class DeleteStockDialog {
    private String title;
    private String message;
    private String negativeButtonText;
    private String positiveButtonText;
    private DialogInterface.OnClickListener negativeButtonListener;
    private DialogInterface.OnClickListener positiveButtonListener;

    public String getTitle() {
        return title;
    }

    public DeleteStockDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public DeleteStockDialog setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public DeleteStockDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DeleteStockDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public DialogInterface.OnClickListener getNegativeButtonListener() {
        return negativeButtonListener;
    }

    public DeleteStockDialog setNegativeButtonListener(DialogInterface.OnClickListener negativeButtonListener) {
        this.negativeButtonListener = negativeButtonListener;
        return this;
    }

    public DialogInterface.OnClickListener getPositiveButtonListener() {
        return positiveButtonListener;
    }

    public DeleteStockDialog setPositiveButtonListener(DialogInterface.OnClickListener positiveButtonListener) {
        this.positiveButtonListener = positiveButtonListener;
        return this;
    }

    public void show() {
        AlertDialog.Builder activeStockDialog = new AlertDialog.Builder(BaseActivity.currentActivity);
        activeStockDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .show();
    }
}
