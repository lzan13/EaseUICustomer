package com.easemob.easeui.customer.application;

import android.content.Context;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * 判断是否为满意度调查类型的消息
     *
     * @param message
     * @return
     */
    public boolean isCtrlTypeMessage(EMMessage message) {
        JSONObject jsonObj = null;
        try {
            jsonObj = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT);
            if (jsonObj.has(CustomerConstants.C_ATTR_CTRLTYPE)) {
                String type = jsonObj.getString(CustomerConstants.C_ATTR_CTRLTYPE);
                if (type.equalsIgnoreCase(CustomerConstants.C_ATTR_INVITEENQUIRY)
                        || type.equalsIgnoreCase(CustomerConstants.C_ATTR_ENQUIRY)) {
                    return true;
                }
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否为用户轨迹类型的消息
     *
     * @param message
     * @return
     */
    public boolean isTrackMessage(EMMessage message) {
        JSONObject jsonObj = null;
        try {
            jsonObj = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_MSGTYPE);
            if (jsonObj.has(CustomerConstants.C_ATTR_TRACK)) {
                return true;
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否为订单消息
     *
     * @param message
     * @return
     */
    public boolean isOrderFormMessage(EMMessage message) {
        JSONObject jsonObj = null;
        try {
            jsonObj = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_MSGTYPE);
            if (jsonObj.has(CustomerConstants.C_ATTR_ORDER)) {
                return true;
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        return false;
    }
}
