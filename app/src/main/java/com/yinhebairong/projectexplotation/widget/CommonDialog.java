package com.yinhebairong.projectexplotation.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.yinhebairong.projectexplotation.R;
import com.yinhebairong.projectexplotation.m.M;

/**
 *  权限管理dialog
 *  Created by liming on 2019/10/14.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {

    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private boolean touchOutSide = false;

    public CommonDialog(Context context) {
        this(context, R.style.VoiceDialog);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CommonDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.content = content;
    }

    public CommonDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.content = content;
        this.listener = listener;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    public CommonDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public CommonDialog setTouchOutSide(boolean ab) {
        this.setCanceledOnTouchOutside(ab);
        this.touchOutSide = ab;
        return this;
    }

    public CommonDialog setCancelables() {
        this.setCancelable(false);
        return this;
    }

    public CommonDialog setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(touchOutSide);
        initView();
    }

    private void initView() {
        TextView contentTxt = findViewById(R.id.content);
        TextView titleTxt = findViewById(R.id.title);
        TextView submitTxt = findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        TextView cancelTxt = findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            if (positiveName.equals(M.M)) submitTxt.setVisibility(View.GONE);
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            if (negativeName.equals(M.M)) cancelTxt.setVisibility(View.GONE);
            cancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setVisibility(View.VISIBLE);
            titleTxt.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
