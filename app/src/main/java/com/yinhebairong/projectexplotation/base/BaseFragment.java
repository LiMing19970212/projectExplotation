package com.yinhebairong.projectexplotation.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
  Created by liming on 2019/10/14.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private Toast toast;

    /**
     * 显示吐司 防止多次点击时多次弹出
     *
     * @param message 显示的消息
     */

    /**
     * baidu 图片转文字
     */

    protected static final int REQUEST_CODE_GENERAL_BASIC = 106;
    AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;

    //   ------------------------------------------   优化  ----------------------------------------
    private String TAG = BaseFragment.class.getSimpleName();
    /**
     * 是否执行了lazyLoad方法
     */
    private boolean isLoaded;
    /**
     * 是否创建了View
     */
    private boolean isCreateView;
    private View mRoot;

    /**
     * 当从另一个activity回到fragment所在的activity
     * 当fragment回调onResume方法的时候，可以通过这个变量判断fragment是否可见，来决定是否要刷新数据
     */
    public boolean isVisible;

    /*
     * 此方法在viewpager嵌套fragment时会回调
     * 查看FragmentPagerAdapter源码中instantiateItem和setPrimaryItem会调用此方法
     * 在所有生命周期方法前调用
     * 这个基类适用于在viewpager嵌套少量的fragment页面
     * 该方法是第一个回调，可以将数据放在这里处理（viewpager默认会预加载一个页面）
     * 只在fragment可见时加载数据，加快响应速度
     * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }


    /*
     *  因为Fragment是缓存在内存中，所以可以保存mRoot ，防止view的重复加载
     *  与FragmentPagerAdapter 中destroyItem方法取消调用父类的效果是一样的，可以任选一种做法
     *  推荐第二种
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = createView(inflater, container, savedInstanceState);
            isCreateView = true;
            unbinder =  ButterKnife.bind(mRoot);
            initView(mRoot);
            initListener();
            onVisible();
        }
        return mRoot;
    }

    //   -------------------------------------------------   优化   ----------------------------------------------------------------
    protected void onInvisible() {
        isVisible = false;
    }

    protected void onVisible() {

        isVisible = true;
        if(isLoaded){
            refreshLoad();
        }
        if (!isLoaded && isCreateView && getUserVisibleHint()) {
            isLoaded = true;
            lazyLoad();
        }
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void initView(View root);
    protected abstract void initListener();

    /**
     * fragment第一次可见的时候回调此方法
     */
    protected abstract void lazyLoad();

    /**
     * 在Fragment第一次可见加载以后，每次Fragment滑动可见的时候会回调这个方法，
     * 子类可以重写这个方法做数据刷新操作
     */
    protected void refreshLoad(){}

    //   ----------------------------------------------------   简用   -------------------------------------------------------------
    protected void showToast(String message) {
        if (getActivity() == null) return;
        if (toast == null) toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        else toast.setText(message);
        toast.show();
    }

    public void startActivitys(Class<?> cls) {
        super.startActivity(new Intent(getActivity(), cls));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放内存资源
//        OCR.getInstance(getActivity()).release();
        unbinder.unbind();
    }

}
