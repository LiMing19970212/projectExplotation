package com.yinhebairong.projectexplotation;

import android.os.Build;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.yinhebairong.projectexplotation.base.BaseActivity;

/**
 * 屏幕管理
 * Created by liming on 2019/10/14.
 */
public class ScreenManager {
    private static ScreenManager instance;
    private ScreenManager() { }
    public synchronized static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    /**
     * 窗口全屏
     */
    public void setFullScreen(boolean isChange, BaseActivity mActivity) {
        if (!isChange) {
            return;
        }
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    /**
     * [沉浸状态栏]
     */
    public void setStatusBar(boolean isChange, BaseActivity mActivity) {
        if (!isChange){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
             mActivity.getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
//             mActivity.getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * [沉浸状态栏]
     */
    public void setStatusBar2(BaseActivity mActivity) {
        Window window = mActivity.getWindow();
        //默认API 最低19
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }
    }
}

