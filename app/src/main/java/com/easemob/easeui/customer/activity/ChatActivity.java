package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.ui.EaseChatFragment;

public class ChatActivity extends BaseActivity {

    private EaseChatFragment mChatFragment;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolbar();
        initChat();

    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        mToolbar.setTitle("客服");
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_text_white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
    }

    /**
     * 初始化聊天界面，这里直接使用EaseUI库的EaseChatFragment界面实现
     */
    private void initChat() {
        mUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        mChatFragment = new EaseChatFragment();
        mChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, mChatFragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // hideTitleBar必须在onStart方法调用，因为EaseChatFragment的titileBar是在onActivityCreate方法里初始化的
        mChatFragment.hideTitleBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 当点击通知栏跳转到聊天界面时，这里的操作保证只有一个聊天界面
        String username = getIntent().getExtras().getString("username");
        if (username.equals(mUsername)) {
            super.onNewIntent(intent);
        } else {
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }
}
