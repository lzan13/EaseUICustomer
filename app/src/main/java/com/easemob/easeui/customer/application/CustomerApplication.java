package com.easemob.easeui.customer.application;

import android.app.Application;
import android.content.Context;


/**
 * Created by lzan13 on 2015/11/6 20:08.
 */
public class CustomerApplication extends Application {

    private Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // CustomerHelper 初始化
        CustomerHelper.getInstance().init(mContext);
    }
}
