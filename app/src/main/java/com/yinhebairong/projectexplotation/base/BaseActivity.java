package com.yinhebairong.projectexplotation.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yinhebairong.projectexplotation.ActivityStackManager;
import com.yinhebairong.projectexplotation.AppDavikActivityUtil;
import com.yinhebairong.projectexplotation.ScreenManager;
import com.yinhebairong.projectexplotation.m.M;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

/*
  Created by liming on 2019/10/14.
 */

public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt {

    private boolean hasGotToken = false;

    public AppDavikActivityUtil appDavikActivityUtil = AppDavikActivityUtil.getScreenManager();
    final String TAG = this.getClass().getSimpleName(); //获取当前activity
    public ScreenManager screenManager;
    private Unbinder bind;

    AlertDialog.Builder alertDialog;

    /**
     * context
     */
    protected Context ctx;
    protected BaseActivity activity;

    /**
     * 是否沉浸状态栏
     */
    public boolean isStatusBar = true;
    /**
     * 是否允许全屏
     **/
    public boolean isFullScreen = true;

    /**
     * 导入布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定事件
     */
    protected abstract void setEvent();

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        screenManager = ScreenManager.getInstance();
        screenManager.setStatusBar(isStatusBar, this);
//        screenManager.setFullScreen(isFullScreen, this);
        M.log(TAG, "--->OnCreate");
        setContentView(getLayoutId()); //布局
        bind = ButterKnife.bind(this);


//        appDavikActivityUtil.addActivity(this); //将布局添加到堆栈中 跟下面的ActivityStackManager功能一样
        activity = this; //上下文
        initView();//初始化
        initData();//数据
        setEvent();//事件
        ctx = this;
        ActivityStackManager.getActivityStackManager().pushActivity(this); //将activity添加到堆栈中


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT); //状态栏透明
            setBarBiack(true); //状态栏字体颜色
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  //透明导航栏
        }

    }

    /**
     * 跳转Activity Flag == true --> finish
     * skip Another Activity
     *
     * @param activity
     * @param cls
     */
    public static void skipAnotherActivity(Activity activity, Class<? extends Activity> cls, boolean flag) {
        activity.startActivity(new Intent(activity, cls));
        if (flag == true) {
            activity.finish();
        }
    }

    /**
     * 退出应用
     * called while exit app
     */
    public void exitLogic() {
        ActivityStackManager.getActivityStackManager().popAllActivity();  //清楚所有栈中的activity
        System.exit(0);
    }

    /**
     * 是否设置沉浸式状态栏
     *
     * @param statusBar
     */
    public void setStatusBar(boolean statusBar) {
        isStatusBar = statusBar;
    }

    /**
     * 是否设置全屏
     *
     * @param fullScreen
     */
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "--->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        bind.unbind();
        appDavikActivityUtil.removeActivity(this);
        Log.i(TAG, "--->onDestroy()");
        super.onDestroy();
        // 释放内存资源
//        OCR.getInstance(this).release(); //百度ocr识别
    }

    /**
     * 状态栏颜色
     *
     * @param t
     */
    public void setBarBiack(boolean t) {
        if (t) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

}
