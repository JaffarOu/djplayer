package com.jf.djplayer.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by JF on 2016/4/9.
 * 所有"Activity"基类
 * 通过类似工程方法模式，实现对子类的框架约束
 *
 */
public abstract class BaseActivity extends FragmentActivity{

//    public static final String WHICH_ACTIVITY = "which_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        打印当前Activity名字，方便调试
        Log.i("which_activity",this.getClass().getName());
    }

    /**
     * 设置Activity布局文件，这是一个工厂方法
     */
    abstract protected void doSetContentView();

    /**
     * 对控件进行初始化，这是一个工厂方法
     */
    abstract protected void widgetsInit();

    /**
     * 其他东西的初始化，这是一个工厂方法
     */
    abstract protected void extrasInit();
}
