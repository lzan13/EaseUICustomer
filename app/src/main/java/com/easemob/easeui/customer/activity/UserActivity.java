package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.util.MLSPUtil;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        init();
        initToolbar();

    }

    private void init() {
        findViewById(R.id.layout_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ChatActivity.class);
                // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
                String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
                intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
                intent.putExtra("skill_group", "shouhou");
                intent.putExtra("order", "2");
                startActivity(intent);
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mToolbar.setTitle("模拟订单");
        mToolbar.setNavigationIcon(R.mipmap.ic_avatar_01);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

}
