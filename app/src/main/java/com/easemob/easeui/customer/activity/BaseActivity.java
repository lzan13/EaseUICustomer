package com.easemob.easeui.customer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by lzan13 on 2015/11/6 20:38.
 * 自定义基类，所有Activity都继承自此类，可以定义一些公用方法和变量
 */
public class BaseActivity extends AppCompatActivity {

    // Activity 在活动界面中的全局变量，用来代替this，在基类中定义是为了省去每个集成此类的 Activity 都定义一次
    public BaseActivity mActivity;

    // Toolbar 在基类中定义是为了省去每个集成此类的 Activity 都定义一次
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

    }
}
