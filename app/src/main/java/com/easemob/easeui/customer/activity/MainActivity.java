package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        initView();
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
        findViewById(R.id.item_07).setOnClickListener(viewListener);
        findViewById(R.id.item_08).setOnClickListener(viewListener);
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

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            String item = "商品";
            switch (v.getId()) {
                case R.id.item_01:
                    item = "商品01";
                    break;
                case R.id.item_02:
                    item = "商品02";
                    break;
                case R.id.item_03:
                    item = "商品03";
                    break;
                case R.id.item_04:
                    item = "商品04";
                    break;
                case R.id.item_05:
                    item = "商品05";
                    break;
                case R.id.item_06:
                    item = "商品06";
                    break;
                case R.id.item_07:
                    item = "商品07";
                    break;
                case R.id.item_08:
                    item = "商品08";
                    break;
            }
            intent.setClass(mActivity, DetailActivity.class);
            intent.putExtra("item", item);
            mActivity.startActivity(intent);
        }
    };
}
