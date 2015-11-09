package com.easemob.easeui.customer.application;

import android.content.Context;

import com.easemob.chat.EMChat;
import com.easemob.easeui.controller.EaseUI;

/**
 * Class ${FILE_NAME}
 * <p/>
 * Created by lzan13 on 2015/11/6 21:03.
 */
public class CustomerHelper {

    // CustomerHelper 单例对象
    private static CustomerHelper instance;

    private EaseUI mEaseUI;

    private CustomerHelper() {
    }


    /**
     * 获取单例对象
     *
     * @return
     */
    public static CustomerHelper getInstance() {
        if (instance == null) {
            instance = new CustomerHelper();
        }
        return instance;
    }

    /**
     * 初始化方法，调用EaseUI的初始化，
     *
     * @param context
     */
    public void init(Context context) {
        // 初始化EaseUI，EaseUI内部初始化了环信的SDK
        EaseUI.getInstance().init(context);

        // 获取EaseUI单例对象，为后边设置做准备
        mEaseUI = EaseUI.getInstance();
    }


    /**
     * 判断当前sdk是否登录成功过
     *
     * @return
     */
    public boolean isLogined() {
        return EMChat.getInstance().isLoggedIn();
    }
}
