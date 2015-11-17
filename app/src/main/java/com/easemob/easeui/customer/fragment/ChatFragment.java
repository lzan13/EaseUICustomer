package com.easemob.easeui.customer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.widget.EaseChatExtendMenu;
import com.easemob.easeui.widget.EaseChatInputMenu;
import com.easemob.easeui.widget.EaseChatMessageList;
import com.easemob.easeui.widget.EaseVoiceRecorderView;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * 自定义ChatFragment类
 */
public class ChatFragment extends Fragment {

    private Activity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mUserId;
    private EaseVoiceRecorderView mVoiceRecorderView;
    private EaseChatMessageList mMessageList;
    private EaseChatInputMenu mInputMenu;

    /**
     * 工厂方法，创建一个ChatFragment的实例，并使用提供的参数
     *
     * @param userid 参数一
     * @return 一个新的ChatFragment实例
     */
    public static ChatFragment newInstance(String userid) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(EaseConstant.EXTRA_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        initView();

        initList();

        initInputMenu();
    }

    private void initView() {
        // 按住说话录音控件
        mVoiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);


    }

    private void initSwipeRefreshLayout() {

    }

    private void initList() {
        mMessageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        //初始化messagelist
        mMessageList.init(mUserId, EMMessage.ChatType.Chat.ordinal(), null);
        //设置item里的控件的点击事件
        mMessageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                Snackbar.make(mActivity.getWindow().getDecorView(), "点击头像", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResendClick(final EMMessage message) {
                Snackbar.make(mActivity.getWindow().getDecorView(), "重发事件", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                Snackbar.make(mActivity.getWindow().getDecorView(), "气泡长按事件", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                return false;
            }
        });
    }

    private void initInputMenu() {
        mInputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        //注册底部菜单扩展栏item
        //传入item对应的文字，图片及点击事件监听，extendMenuItemClickListener实现EaseChatExtendMenuItemClickListener
        mInputMenu.registerExtendMenuItem(R.string.app_name, R.mipmap.ic_launcher, 11, new MyItemClickListener());

        //初始化，此操作需放在registerExtendMenuItem后
        mInputMenu.init();
        //设置相关事件监听
        mInputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                // 发送文本消息
//                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                //把touch事件传入到EaseVoiceRecorderView 里进行录音
                return mVoiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }
        });
    }



    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener{

        @Override
        public void onClick(int itemId, View view) {
            if(chatFragmentListener != null){
                if(chatFragmentListener.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }
            switch (itemId) {
                case 11: //

                    break;
                case 12:

                    break;
                default:
                    break;
            }
        }

    }

    //发送消息方法
    //==========================================================================
    protected void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, mUserId);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, mUserId);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, mUserId);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, mUserId);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, mUserId);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, mUserId);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message){
        if(chatFragmentListener != null){
            //设置扩展属性
            chatFragmentListener.onSetMessageAttributes(message);
        }

        //发送消息
        EMChatManager.getInstance().sendMessage(message, null);
        //刷新ui
        mMessageList.refreshSelectLast();
    }


    public void resendMessage(EMMessage message){
        message.status = EMMessage.Status.CREATE;
        EMChatManager.getInstance().sendMessage(message, null);
        mMessageList.refresh();
    }

    protected ChatFragmentListener chatFragmentListener;
    public void setChatFragmentListener(ChatFragmentListener chatFragmentListener){
        this.chatFragmentListener = chatFragmentListener;
    }

    public interface ChatFragmentListener{
        /**
         * 设置消息扩展属性
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * 进入会话详情
         */
        void onEnterToChatDetails();

        /**
         * 用户头像点击事件
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * 消息气泡框点击事件
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * 消息气泡框长按事件
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * 设置自定义chatrow提供者
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }
}
