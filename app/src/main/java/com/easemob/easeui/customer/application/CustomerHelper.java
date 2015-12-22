package com.easemob.easeui.customer.application;

import android.content.Context;
import android.content.Intent;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.activity.ChatActivity;
import com.easemob.easeui.customer.util.MLSPUtil;
import com.easemob.easeui.model.EaseNotifier;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lzan13 on 2015/11/6 21:03.
 * 单例类，用来初始化EaseUI 对EaseUI进行配置
 */
public class CustomerHelper {

    private Context mContext;

    // CustomerHelper 单例对象
    private static CustomerHelper instance;

    // EaseUI的实例
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
        mContext = context;
        EMChat.getInstance().setAppkey((String) MLSPUtil.get(context, CustomerConstants.C_APPKEY, ""));
        // 初始化EaseUI，EaseUI内部初始化了环信的SDK
        EaseUI.getInstance().init(context);

        // 获取EaseUI单例对象，为后边设置做准备
        mEaseUI = EaseUI.getInstance();

        // 初始化设置EaseUI定义的一些提供者
        setEaseUIProviders();
        // 全局监听消息方法
        registerEventListener();
    }

    /**
     * 设置EaseUI的提供者
     */
    public void setEaseUIProviders() {

        /**
         * 设置通知消息提供者
         */
        mEaseUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, mContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                if (message.getFrom().equals(MLSPUtil.get(mContext, CustomerConstants.C_IM, ""))) {
                    return "客服: " + ticker;
                } else {
                    return message.getFrom() + ": " + ticker;
                }
            }

            /**
             * 根据消息条数来判断如果显示
             * @param message
             *            接收到的消息
             * @param fromUsersNum
             *            发送人的数量
             * @param messageNum
             *            消息数量
             * @return
             */
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // 当只有一个人，发来一条消息时，显示消息内容 TODO 表情符显示为图片
                if (fromUsersNum == 1 && messageNum == 1) {
                    return "客服：" + EaseCommonUtils.getMessageDigest(message, mContext).replace("\\[.{2,3}\\]", "[表情]");
                } else {
                    return fromUsersNum + " 个联系人，发来 " + messageNum + " 条消息";
                }
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            /**
             * 设置通知栏小图标，规定要求大小为24dp
             * @param message
             * @return
             */
            @Override
            public int getSmallIcon(EMMessage message) {
                return R.mipmap.ic_small;
            }

            /**
             * 通知栏点击跳转设置，这里因为只有客服，所以没有其他判断，直接跳转到聊天界面，
             * 把客服username传递过去即可
             * @param message
             *            显示在notification上最近的一条消息
             * @return
             */
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent = new Intent();
                intent.setClass(mContext, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                return intent;
            }
        });
    }


    /**
     * 全局的消息监听事件，这里的监听会一直执行，所以要判断一下程序是否在前台运行阶段，
     * 如果在前台就让前台界面的监听去处理消息（比如MainActivity、ChatActivity里的onEvent）
     */
    private void registerEventListener() {
        EMEventListener eventListener = new EMEventListener() {
            @Override
            public void onEvent(EMNotifierEvent emNotifierEvent) {
                switch (emNotifierEvent.getEvent()) {
                    case EventNewMessage:
                        // 判断如果在前台活动，全局的监听就不处理消息通知
                        if (!mEaseUI.hasForegroundActivies()) {
                            EMMessage message = (EMMessage) emNotifierEvent.getData();
                            getNotifier().onNewMsg(message);
                        }
                        break;
                    case EventOfflineMessage:
                        // 判断如果在前台活动，全局的监听就不处理消息通知
                        if (!mEaseUI.hasForegroundActivies()) {
                            List<EMMessage> messageList = (List<EMMessage>) emNotifierEvent.getData();
                            getNotifier().onNewMesg(messageList);
                        }
                        break;
                }

            }
        };
        EMChatManager.getInstance().registerEventListener(eventListener);
    }

    /**
     * 获取EaseUI定义的通知对象，用来发送通知
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return mEaseUI.getNotifier();
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 退出登陆方法
     */
    public void signOut(final EMCallBack callback) {
        EMChatManager.getInstance().logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                mEaseUI = null;
                callback.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callback.onError(i, s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
