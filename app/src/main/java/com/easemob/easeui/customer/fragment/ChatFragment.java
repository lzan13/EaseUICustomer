package com.easemob.easeui.customer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseChatExtendMenu;

/**
 * 自定义ChatFragment类
 */
public class ChatFragment extends EaseChatFragment {

    private Activity mActivity;

    private CustomerFragmentListener mFragmentListener;

    private String mUserId;
    private ListView mAnswerListView;

    protected int[] itemStrings = {R.string.input_menu_camera, R.string.input_menu_photo, R.string.input_menu_answer};
    protected int[] itemdrawables = {R.drawable.btn_customer_camera, R.drawable.btn_customer_photo, R.drawable.btn_customer_answer};
    protected int[] itemIds = {1, 2, 11};


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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initView();
    }


    /**
     * 注册扩展菜单，这里不调用父类的方法
     */
    @Override
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], new CustomerItemClickListener());
        }
    }


    /**
     * 扩展菜单栏item点击事件
     */
    class CustomerItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentListener != null) {
                if (chatFragmentListener.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
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
        }
    }

    /**
     * 发送常用回复
     *
     * @param content
     */
    public void sendAnswer(String content) {
        sendTextMessage(content);
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
     * ChatFragment 回调函数，由Activity实现，用来和Activity实现通讯
     */
    public interface CustomerFragmentListener {
        public void onFragmentInteraction(int i);

    }
}
