package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;

public class DetailActivity extends BaseActivity {

    private View mChatView;
    private View mJoinInShoppingCartView;
    private View mBuyNowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initToolbar();
        initView();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        mToolbar.setTitle(R.string.title_activity_detail);
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
     * 初始化商品详情UI控件
     */
    private void initView() {
        mChatView = findViewById(R.id.action_chat);
        mJoinInShoppingCartView = findViewById(R.id.action_join_in_shopping_cart);
        mBuyNowView = findViewById(R.id.action_buy_now);

        mChatView.setOnClickListener(viewListener);
        mJoinInShoppingCartView.setOnClickListener(viewListener);
        mBuyNowView.setOnClickListener(viewListener);
    }


    /**
     * 界面控件点击事件
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_chat:
                    // 联系客服，跳转到与客服聊天界面
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ChatActivity.class);
                    // 这个在使用EaseChatFragment时，传入的username参数key必须用EaseUI定义的，否则会报错
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, "lz_customer");
                    mActivity.startActivity(intent);
                    break;
                case R.id.action_join_in_shopping_cart:
                    // 加入购物车弹出，无具体实现
                    Snackbar.make(mJoinInShoppingCartView, "", Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.action_buy_now:
                    // 立即购买弹出提示，无具体操作
                    Snackbar.make(mBuyNowView, "", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };


}
