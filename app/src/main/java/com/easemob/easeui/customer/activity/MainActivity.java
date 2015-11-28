package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.application.CustomerHelper;
import com.easemob.easeui.customer.util.MLSPUtil;

/**
 * Created by lzan13 on 2015/11/6 21:10.
 * 程序主界面，主要模拟实现商品的列表展示，以及设置界面的跳转等功能
 * TODO (lzan13) 聊天信息的监听，
 */
public class MainActivity extends BaseActivity implements EMEventListener {

    private View mLayoutFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        String nickName = (String) MLSPUtil.get(mActivity, CustomerConstants.C_USER_KEY_USERNICKNAME, "");
//        mToolbar.setTitleTextColor(R.color.ml_text_primary);
        mToolbar.setTitle(nickName);
        mToolbar.setNavigationIcon(R.mipmap.ic_avatar_01);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mActivity.getWindow().getDecorView(), "暂未实现修改用户信息界面", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化UI
     */
    private void initView() {
        findViewById(R.id.item_01).setOnClickListener(viewListener);
        findViewById(R.id.item_02).setOnClickListener(viewListener);
        findViewById(R.id.item_03).setOnClickListener(viewListener);
        findViewById(R.id.item_04).setOnClickListener(viewListener);
        findViewById(R.id.item_05).setOnClickListener(viewListener);
        findViewById(R.id.item_06).setOnClickListener(viewListener);
        mLayoutFab = findViewById(R.id.layout_fab);
        mLayoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpCustomer();
            }
        });
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    private void updateUnreadmessageAlert() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = EMChatManager.getInstance().getUnreadMsgsCount();
                if (count > 0) {
                    mLayoutFab.setBackgroundResource(R.drawable.btn_fab_red);
                } else {
                    mLayoutFab.setBackgroundResource(R.drawable.btn_fab_green);
                }
            }
        });
    }


    /**
     * 初始化菜单方法
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu菜单项点击方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
//            case R.id.action_message:
//                // 点击直接和客服联系
//                intent.setClass(mActivity, ChatActivity.class);
//                // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
//                String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
//                mActivity.startActivity(intent);
//                break;
            case R.id.action_setting:
                // 进入设置界面
                intent.setClass(mActivity, SettingActivity.class);
                mActivity.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 跳转到客服界面
     */
    private void jumpCustomer() {
        Intent intent = new Intent();
        // 点击直接和客服联系
        intent.setClass(mActivity, ChatActivity.class);
        // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
        String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
        intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
        mActivity.startActivity(intent);
    }

    /**
     * 商品列表点击事件，这里是跳转到商品详情页，并传递参数
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            String item = "item";
            switch (v.getId()) {
                case R.id.item_01:
                    item = "1";
                    break;
                case R.id.item_02:
                    item = "2";
                    break;
                case R.id.item_03:
                    item = "3";
                    break;
                case R.id.item_04:
                    item = "4";
                    break;
                case R.id.item_05:
                    item = "5";
                    break;
                case R.id.item_06:
                    item = "6";
                    break;
                default:
                    item = "6";
                    break;
            }
            intent.setClass(mActivity, DetailActivity.class);
            intent.putExtra("item", item);
            mActivity.startActivity(intent);
        }
    };

    /**
     * 实现消息监听
     *
     * @param emNotifierEvent
     */
    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        switch (emNotifierEvent.getEvent()) {
            case EventNewMessage:
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                CustomerHelper.getInstance().getNotifier().onNewMsg(message);
                updateUnreadmessageAlert();
                break;
            case EventOfflineMessage:
                updateUnreadmessageAlert();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 更新未读消息数
        updateUnreadmessageAlert();
        // 注册消息监听，主要监听客服发来的正常消息和离线消息
        EMNotifierEvent.Event[] events = {
                EMNotifierEvent.Event.EventNewMessage,
                EMNotifierEvent.Event.EventOfflineMessage
        };
        EMChatManager.getInstance().registerEventListener(this, events);
    }

    @Override
    protected void onStop() {
        // 注销消息监听
        EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }
}
