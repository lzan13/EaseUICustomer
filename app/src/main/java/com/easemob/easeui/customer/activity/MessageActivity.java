package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.util.MLSPUtil;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();
        initToolbar();

    }

    private void init() {
        findViewById(R.id.layout_customer).setOnClickListener(viewListener);
        findViewById(R.id.layout_order).setOnClickListener(viewListener);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mToolbar.setTitle("模拟订单");
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_customer:
                    jumpCustomer(null, "shouhou");
                    break;
                case R.id.layout_order:
                    jumpCustomer("2", "shouhou");
                    break;
            }

        }
    };

    /**
     * 跳转到客服界面
     */
    private void jumpCustomer(String order, String skillGroup) {
        Intent intent = new Intent();
        // 点击直接和客服联系
        intent.setClass(mActivity, ChatActivity.class);
        // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
        String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
        intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
        // 这里默认直接联系客服的 “shouhou” 技能组
        intent.putExtra("skill_group", "shouhou");
        if (order != null) {
            intent.putExtra("order", order);
        }
        mActivity.startActivity(intent);
    }
}
