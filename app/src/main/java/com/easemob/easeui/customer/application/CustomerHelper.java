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

    private static CustomerHelper instance;

    private EaseUI mEaseUI;

    private CustomerHelper() {
    }


    public static CustomerHelper getInstance() {
        if (instance == null) {
            instance = new CustomerHelper();
        }
        return instance;
    }

    public void init(Context context) {
        EaseUI.getInstance().init(context);
        mEaseUI = EaseUI.getInstance();
    }



    public boolean isLogined() {
        return EMChat.getInstance().isLoggedIn();
    }
}
