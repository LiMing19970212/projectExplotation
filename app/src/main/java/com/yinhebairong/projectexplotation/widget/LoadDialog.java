package com.yinhebairong.projectexplotation.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yinhebairong.projectexplotation.R;


/**
 *  Created by liming on 2019/10/14.
 */


public class LoadDialog extends Dialog {
    private static LoadDialog customProgressDialog = null;
    private static ProgressBar loadprogress;//进度框
    private static RelativeLayout loadtag;//父布局
    private static TextView loadtxt;//根文字

    public LoadDialog(@NonNull Context context) {
        super(context);
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static LoadDialog createDialog(Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
            customProgressDialog = null;
        }
        customProgressDialog = new LoadDialog(context, R.style.VoiceDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.base_prodialog, null);
        customProgressDialog.setContentView(contentView);
        loadprogress = contentView.findViewById(R.id.load_progress);
        loadtag = contentView.findViewById(R.id.load_tag);
        loadtxt = contentView.findViewById(R.id.load_txt);
//        透明色
        loadtag.getBackground().setAlpha(153);
//        渲染颜色
        loadprogress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.MULTIPLY);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCanceledOnTouchOutside(false);//点击外部不消失
        return customProgressDialog;
    }

}

