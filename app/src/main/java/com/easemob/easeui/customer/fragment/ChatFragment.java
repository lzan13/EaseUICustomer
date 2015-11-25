package com.easemob.easeui.customer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerHelper;
import com.easemob.easeui.customer.entity.ShopEntity;
import com.easemob.easeui.customer.widget.CtrlTypeChatRow;
import com.easemob.easeui.customer.widget.OrderChatRow;
import com.easemob.easeui.customer.widget.TrackChatRow;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseChatExtendMenu;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义ChatFragment类
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentListener {

    private Activity mActivity;

    private CustomerFragmentListener mFragmentListener;

    private String mCurrentItem;

    private String mUserId;
    private ListView mAnswerListView;

    protected int[] itemStrings = {R.string.input_menu_camera, R.string.input_menu_photo, R.string.input_menu_answer};
    protected int[] itemdrawables = {R.drawable.btn_customer_camera, R.drawable.btn_customer_photo, R.drawable.btn_customer_answer};
    protected int[] itemIds = {1, 2, 11};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(EaseConstant.EXTRA_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mCurrentItem = getArguments().getString("item");
        if (mCurrentItem != null) {
            sendTrackMessage(Integer.valueOf(mCurrentItem));
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


    @Override
    public void onSetMessageAttributes(EMMessage message) {

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
            case 3: // 位置
//                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                break;
            case 11:// 常用语
                mFragmentListener.onFragmentInteraction(11);
                break;
            default:

                break;
        }
        return false;
    }


    /**
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
         * 返回自定义消息的个数
         *
         * @return
         */
        @Override
        public int getCustomChatRowTypeCount() {
            return 4;
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
                    return message.direct == EMMessage.Direct.RECEIVE ? 1 : 2;
                } else if (CustomerHelper.getInstance().isTrackMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 3 : 4;
                } else if (CustomerHelper.getInstance().isOrderFormMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 5 : 6;
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
                    return new CtrlTypeChatRow(mActivity, message, position, adapter);
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
     * ChatFragment 回调函数，由Activity实现，用来和Activity实现通讯
     */
    public interface CustomerFragmentListener {
        public void onFragmentInteraction(int i);

    }

    /**
     * 发送常用回复
     *
     * @param content
     */
    public void sendAnswer(String content) {
        sendTextMessage(content);
    }

    public void sendTrackMessage(int item) {
        ShopEntity shopEntity = new ShopEntity(item);

        EMMessage message = EMMessage.createTxtSendMessage("客服图文混排消息", toChatUsername);
        JSONObject jsonMsgType = new JSONObject();
        JSONObject jsonTrack = new JSONObject();
        try {
            jsonTrack.put("title", shopEntity.getShopTitle());
            jsonTrack.put("price", shopEntity.getShopPrice());
            jsonTrack.put("desc", shopEntity.getShopDesc());
            jsonTrack.put("img_url", shopEntity.getShopImageUrl());
            jsonTrack.put("item_url", shopEntity.getShopUrl());
            jsonMsgType.put("track", jsonTrack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("msgtype", jsonMsgType);
        sendMessage(message);
    }
}
