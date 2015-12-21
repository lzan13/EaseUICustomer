package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.entity.ShopEntity;
import com.easemob.easeui.customer.util.MLSPUtil;

/**
 * Created by lzan13 on 2015/11/7 21:00.
 * 商品详情类
 */
public class DetailActivity extends BaseActivity {

    // 这里用webview实现模拟加载商品详情，
    private WebView mWebView;
    private ShopEntity mShopEntity;
    // 当前选择的商品
    private String mCurrentShop;

    private View mChatView;
    private View mJoinInShoppingCartView;
    private View mBuyNowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        initToolbar();
        initView();
    }

    /**
     * 初始化数据，这里是根据点击进来的 shop 去初始化模拟商品详情
     */
    private void init() {
        mCurrentShop = getIntent().getStringExtra("shop");
        mShopEntity = new ShopEntity(Integer.valueOf(mCurrentShop));
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mToolbar.setTitle(mShopEntity.getTitle());
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
        mWebView = (WebView) findViewById(R.id.webview_detail);
        WebSettings settings = mWebView.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.loadUrl("file:///android_asset/shop/shop_0" + mShopEntity.getItem() + ".html");

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
                    String username = (String) MLSPUtil.get(mActivity, CustomerConstants.C_IM, "");
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
                    intent.putExtra("shop", mCurrentShop);
                    // 这里默认的客服技能组是 “shouqian”
                    intent.putExtra("skill_group", "shouqian");
                    mActivity.startActivity(intent);
                    break;
                case R.id.action_join_in_shopping_cart:
                    // 加入购物车弹出，无具体实现
                    Snackbar.make(mActivity.getWindow().getDecorView(), "暂时不支持加入购物车", Snackbar.LENGTH_SHORT).show();
                    break;
                case R.id.action_buy_now:
                    // 立即购买弹出提示，无具体操作
                    Snackbar.make(mActivity.getWindow().getDecorView(), "暂时不支持购买", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
