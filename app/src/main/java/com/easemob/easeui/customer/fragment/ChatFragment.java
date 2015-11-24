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
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseChatExtendMenu;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * 自定义ChatFragment类
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentListener {

    private Activity mActivity;

    private CustomerFragmentListener mFragmentListener;

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
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        super.setUpView();
    }

    /**
     * 发送常用回复
     *
     * @param content
     */
    public void sendAnswer(String content) {
        sendTextMessage(content);
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
     * 消息框点击事件
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

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
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

    /**
     * chat row provider
     */
    class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {

            return 4;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (CustomerHelper.getInstance().isCtrlTypeMessage(message)) {

                } else if (CustomerHelper.getInstance().isTrackMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 1 : 2;
                } else if (CustomerHelper.getInstance().isOrderFormMessage(message)) {
                    return message.direct == EMMessage.Direct.RECEIVE ? 1 : 2;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {

            return null;
        }
    }

    /**
     * ChatFragment 回调函数，由Activity实现，用来和Activity实现通讯
     */
    public interface CustomerFragmentListener {
        public void onFragmentInteraction(int i);

    }
}
