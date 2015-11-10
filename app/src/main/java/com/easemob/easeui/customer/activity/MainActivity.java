package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.util.MLSPUtil;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        String nickName = (String) MLSPUtil.get(mActivity, CustomerConstants.C_USERNICKNAME, "");
        mToolbar.setTitle(nickName);
        mToolbar.setNavigationIcon(R.mipmap.ic_avatar_01);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu项点击方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_message:
                intent.setClass(mActivity, ChatActivity.class);
                // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
                String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
                intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
                mActivity.startActivity(intent);
                break;
            case R.id.action_setting:
                intent.setClass(mActivity, SettingActivity.class);
                mActivity.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
