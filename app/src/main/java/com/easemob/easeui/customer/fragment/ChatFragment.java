package com.easemob.easeui.customer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.application.CustomerHelper;
import com.easemob.easeui.customer.entity.EnquiryEntity;
import com.easemob.easeui.customer.entity.OrderEntity;
import com.easemob.easeui.customer.entity.ShopEntity;
import com.easemob.easeui.customer.util.MLSPUtil;
import com.easemob.easeui.customer.widget.CtrlTypeChatRow;
import com.easemob.easeui.customer.widget.OrderChatRow;
import com.easemob.easeui.customer.widget.TrackChatRow;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2015/11/8 21:30.
 * 自定义ChatFragment类
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentListener {

    private Activity mActivity;


    // ChatFragment 回调函数，由Activity实现，用来和Activity实现通讯
    private CustomerFragmentListener mFragmentListener;

    /**
     * 当前选择的哪一件商品，这里只是测试，为了从ShopEntity获取商品信息通过扩展发送轨迹类型消息
     * 这个只有在从商品详情页点击进来才会有值，
     */
    private String mCurrentShop;

    /**
     * 模拟当前选择的订单
     */
    private String mCurrentOrder;

    // 技能组
    private String mSkillGroup;

    // 扩展菜单数据
    protected int[] itemStrings = {R.string.btn_input_menu_camera, R.string.btn_input_menu_photo, R.string.btn_input_menu_answer};
    protected int[] itemdrawables = {R.drawable.btn_customer_camera, R.drawable.btn_customer_photo, R.drawable.btn_customer_answer};
    protected int[] itemIds = {1, 2, 3};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mActivity.getFilesDir().getPath();
        // 模拟从哪一件商品进来联系客服
        mCurrentShop = getArguments().getString("shop");
        mCurrentOrder = getArguments().getString("order");
        mSkillGroup = getArguments().getString("skill_group");

        if (mCurrentShop != null) {
            // 如果商品扩展不为null 则默认发送一条轨迹消息
            sendTrackMessage(Integer.valueOf(mCurrentShop));
        } else if (mCurrentOrder != null) {
            // 如果是从订单页点击进来的，则默认发送一条订单消息
            sendOrderMessage(Integer.valueOf(mCurrentOrder));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentListener = (CustomerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        super.setUpView();
    }


    /**
     * 注册扩展菜单，这里不调用父类的方法
     */
    @Override
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    /**
     * 设置消息扩展
     *
     * @param message
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        // 设置用户信息扩展
        setUserAttibutes(message);
        // 调用设置技能组扩展
        setSkillGroup(message);
    }

    @Override
    public void onEnterToChatDetails() {

    }

    /**
     * 头像点击事件
     *
     * @param username
     */
    @Override
    public void onAvatarClick(String username) {

    }

    /**
     * 消息气泡点击事件
     *
     * @param message
     * @return
     */
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    /**
     * 扩展菜单栏item点击事件
     *
     * @param itemId
     * @param view
     * @return
     */
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case 1: // 拍照
                selectPicFromCamera();
                break;
            case 2:
                selectPicFromLocal(); // 图库选择图片
                break;
            case 3:// 常用语
                mFragmentListener.onFragmentInteraction(11);
                break;
            case 10: // 位置
//                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                break;
            default:

                break;
        }
        return true;
    }

    /**
     * ---------------------------------------------------
     * 设置自定义消息提供者
     *
     * @return
     */
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    /**
     * 自定义实现ChatRow提供者
     */
    class CustomChatRowProvider implements EaseCustomChatRowProvider {
        /**
         * 返回自定义消息的个数(这个个数必须和你自定义的ChatRow界面个数一致，否则会数组越界)
         * FATAL EXCEPTION: main Process: com.easemob.easeui.customer, PID: 32217
         * java.lang.ArrayIndexOutOfBoundsException: length=18; index=18
         * at android.widget.AbsListView$RecycleBin.addScrapView(AbsListView.java:6588)
         *
         * @return
         */
        @Override
        public int getCustomChatRowTypeCount() {
            return 6;
        }

        /**
         * 返回消息的类型
         *
         * @param message
         * @return
         */
        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (CustomerHelper.getInstance().isCtrlTypeMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 2 : 1;
                } else if (CustomerHelper.getInstance().isTrackMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 4 : 3;
                } else if (CustomerHelper.getInstance().isOrderFormMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 6 : 5;
                }
            }
            return 0;
        }

        /**
         * 返回自定义消息的实现
         *
         * @param message
         * @param position
         * @param adapter
         * @return
         */
        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (CustomerHelper.getInstance().isCtrlTypeMessage(message)) {
                    CtrlTypeChatRow ctrlTypeChatRow = new CtrlTypeChatRow(mActivity, message, position, adapter);
                    ctrlTypeChatRow.setmChatRowListener(new MyChatRowListener());
                    return ctrlTypeChatRow;
                } else if (CustomerHelper.getInstance().isTrackMessage(message)) {
                    return new TrackChatRow(mActivity, message, position, adapter);
                } else if (CustomerHelper.getInstance().isOrderFormMessage(message)) {
                    return new OrderChatRow(mActivity, message, position, adapter);
                }
            }
            return null;
        }
    }


    /**
     * 设置用户信息扩展
     *
     * @param message
     */
    private void setUserAttibutes(EMMessage message) {
        boolean isFrist = (boolean) MLSPUtil.get(mActivity, CustomerConstants.C_IS_FIRST_CUSTOMER, false);
        if (isFrist) {
            JSONObject jsonWeiChat = getWeichatJSONObject(message);
            JSONObject jsonVisitor = new JSONObject();
            try {
                jsonVisitor.put(CustomerConstants.C_USER_KEY_TRUENAME,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_TRUENAME, ""));
                jsonVisitor.put(CustomerConstants.C_USER_KEY_COMPANYNAME,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_COMPANYNAME, ""));
                jsonVisitor.put(CustomerConstants.C_USER_KEY_USERNICKNAME,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_USERNICKNAME, ""));
                jsonVisitor.put(CustomerConstants.C_USER_KEY_DESCRIPTION,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_DESCRIPTION, ""));
                jsonVisitor.put(CustomerConstants.C_USER_KEY_QQ,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_QQ, ""));
                jsonVisitor.put(CustomerConstants.C_USER_KEY_EMAIL,
                        MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_EMAIL, ""));
                jsonWeiChat.put("visitor", jsonVisitor);
                message.setAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT, jsonWeiChat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 调用设置用户信息扩展
            MLSPUtil.put(mActivity, CustomerConstants.C_IS_FIRST_CUSTOMER, false);
        }
    }

    /**
     * 设置技能组
     *
     * @param message
     */
    private void setSkillGroup(EMMessage message) {
        try {
            JSONObject jsonWebChat = getWeichatJSONObject(message);
            // 这里是根据传进来的 商品 是否为空确定是从首页点击连接客服，还是从商品详情页点击联系客服
            if (mCurrentShop != null) {
                jsonWebChat.put("queueName", "shouqian");
            } else if (mCurrentOrder != null) {

            } else {
                jsonWebChat.put("queueName", "shouhou");
            }
            message.setAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT, jsonWebChat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送常用回复
     *
     * @param content
     */
    public void sendAnswer(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        setUserAttibutes(message);
        setSkillGroup(message);
        sendMessage(message);
    }

    /**
     * 发送包含用户轨迹信息
     *
     * @param item
     */
    public void sendTrackMessage(int item) {
        ShopEntity shopEntity = new ShopEntity(item);

        EMMessage message = EMMessage.createTxtSendMessage("客服图文混排消息", toChatUsername);
        JSONObject jsonMsgType = new JSONObject();
        JSONObject jsonTrack = new JSONObject();
        try {
            jsonTrack.put("title", shopEntity.getTitle());
            jsonTrack.put("price", shopEntity.getPrice());
            jsonTrack.put("desc", shopEntity.getDesc());
            jsonTrack.put("img_url", shopEntity.getImageUrl());
            jsonTrack.put("item_url", shopEntity.getUrl());
            jsonMsgType.put("track", jsonTrack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("msgtype", jsonMsgType);
        setUserAttibutes(message);
        setSkillGroup(message);
        sendMessage(message);
    }

    /**
     * 发送包含订单信息的消息
     *
     * @param item
     */
    public void sendOrderMessage(int item) {
        OrderEntity orderEntity = new OrderEntity(item);

        EMMessage message = EMMessage.createTxtSendMessage("客服图文混排消息", toChatUsername);
        JSONObject jsonMsgType = new JSONObject();
        JSONObject jsonTrack = new JSONObject();
        try {
            jsonTrack.put("title", orderEntity.getTitle());
            jsonTrack.put("order_title", orderEntity.getOrderTitle());
            jsonTrack.put("price", orderEntity.getPrice());
            jsonTrack.put("desc", orderEntity.getDesc());
            jsonTrack.put("img_url", orderEntity.getImageUrl());
            jsonTrack.put("item_url", orderEntity.getUrl());
            jsonMsgType.put("order", jsonTrack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("msgtype", jsonMsgType);
        setUserAttibutes(message);
        setSkillGroup(message);
        sendMessage(message);
    }

    /**
     * 获取消息中的扩展 weichat是否存在，并返回jsonObject
     *
     * @param message
     * @return
     */
    private JSONObject getWeichatJSONObject(EMMessage message) {
        JSONObject jsonWeiChat = null;
        try {
            jsonWeiChat = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT);
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        if (jsonWeiChat == null) {
            jsonWeiChat = new JSONObject();
        }
        return jsonWeiChat;
    }


    /**
     * 清空当前会话聊天记录
     */
    public void clearConversation() {
        emptyHistory();
    }

    /**
     * ------------------------------------------------------------------
     * <p/>
     * 满意度评价的回调接口，为了实现客户端在满意度ChatRow中发送满意度类型的消息
     */
    class MyChatRowListener implements CtrlTypeChatRow.CustomerChatRowListener {

        @Override
        public void onChatRowInteraction(EnquiryEntity enquiryEntity) {
            EMMessage message = EMMessage.createTxtSendMessage("客服图文混排消息", toChatUsername);
            JSONObject jsonWeiChat = new JSONObject();
            JSONObject jsonCtrlArgs = new JSONObject();
            try {
                jsonCtrlArgs.put("inviteId", enquiryEntity.getInviteId());
                jsonCtrlArgs.put("serviceSessionId", enquiryEntity.getServiceSessionId());
                jsonCtrlArgs.put("detail", enquiryEntity.getDetail());
                jsonCtrlArgs.put("summary", enquiryEntity.getSummary());

                jsonWeiChat.put("ctrlType", "enquiry");
                jsonWeiChat.put("ctrlArgs", jsonCtrlArgs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            message.setAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT, jsonWeiChat);
            sendMessage(message);
        }
    }

    /**
     * ----------------------------------------------------------------
     * <p/>
     * ChatFragment 回调接口，由Activity实现，用来和Activity实现通讯
     */
    public interface CustomerFragmentListener {
        public void onFragmentInteraction(int i);
    }
}
