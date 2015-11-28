package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerHelper;
import com.easemob.easeui.customer.fragment.ChatFragment;

/**
 * Created by lzan13 on 2015/11/6 21:30.
 * 聊天界面，这里只是个容器，真正实现聊天是在ChatFragment类里{@link ChatFragment}
 */
public class ChatActivity extends BaseActivity implements ChatFragment.CustomerFragmentListener {

    // 通过Fragment的方式加聊天界面
    private ChatFragment mChatFragment;
    // 会话者
    private String mUsername;
    // 常用语View
    private View mAnswerView;
    // 常用语关闭按钮
    private Button mCloseBtn;
    // 常用语展示listview
    private ListView mAnswerListView;

    // 模拟常用回复数据
    private String[] mAnswers = {"亲，你们包邮么？", "默认发什么快递呢？", "亲，这件还有货么？", "亲，希望赶快发货哦！"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolbar();
        initChat();
        initAnswerLayout();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        mToolbar.setTitle("客服");
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_text_white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化聊天界面，这里直接使用EaseUI库的EaseChatFragment界面实现
     */
    private void initChat() {
        mUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        mChatFragment = new ChatFragment();
        mChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, mChatFragment).commit();

    }

    /**
     * 初始化常用语布局
     */
    private void initAnswerLayout() {
        mAnswerView = findViewById(R.id.layout_answer);
        mCloseBtn = (Button) findViewById(R.id.btn_answer_close);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerView.setVisibility(View.GONE);
            }
        });
        mAnswerListView = (ListView) findViewById(R.id.answer_list);
        mAnswerListView.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_expandable_list_item_1, mAnswers));
        mAnswerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChatFragment.sendAnswer(mAnswers[position]);
                mAnswerView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 进入聊天界面重置通知栏消息数量
        CustomerHelper.getInstance().getNotifier().reset();
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

    /**
     * 实现ChatFragment的回调方法，用来给Fragment调用，实现Activity和Fragment的通讯
     *
     * @param i
     */
    @Override
    public void onFragmentInteraction(int i) {
        switch (i) {
            case 11:
                mAnswerView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
