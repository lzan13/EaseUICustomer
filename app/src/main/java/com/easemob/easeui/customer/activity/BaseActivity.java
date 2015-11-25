package com.easemob.easeui.customer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by lzan13 on 2015/11/6 20:58.
 * 自定义基类，所有Activity都继承自此类，可以定义一些公用方法和变量
 */
public class BaseActivity extends AppCompatActivity {

    // Activity 全局变量用来代替this
    public BaseActivity mActivity;

    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

    }
}
