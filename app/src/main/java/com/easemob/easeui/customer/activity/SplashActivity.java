package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.easemob.chat.EMChatManager;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.application.CustomerHelper;
import com.easemob.easeui.customer.util.MLSPUtil;

public class SplashActivity extends BaseActivity {

    // 开屏页持续时间
    private int mTime = 3000;
    // 动画持续时间
    private int mDurationTime = 1500;

    // 开屏页显示的图片控件
    private View mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

    }

    /**
     * 初始化当前界面的控件
     */
    private void initView() {
        mImageView = findViewById(R.id.img_splash);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(mDurationTime);
        mImageView.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            public void run() {
                if (CustomerHelper.getInstance().isLogined()) {
                    // 获取当前系统时间毫秒数
                    long start = System.currentTimeMillis();
                    // 加载本地会话到内存
                    EMChatManager.getInstance().loadAllConversations();
                    // 获取加载回话使用的时间差 毫秒表示
                    long costTime = System.currentTimeMillis() - start;
                    if (mTime - costTime > 0) {
                        try {
                            Thread.sleep(mTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 进入主页面
                    startActivity(new Intent(mActivity, MainActivity.class));
                } else {
                    try {
                        // 初始化数据
                        initData();
                        // 睡眠3000毫秒
                        Thread.sleep(mTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 跳转到登录界面
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
                finish();
            }
        }).start();
    }

    /**
     * 初始化app数据
     */
    private void initData() {
        MLSPUtil.put(mActivity, CustomerConstants.C_APPKEY, "lzan13#hxsdkdemo");
        MLSPUtil.put(mActivity, CustomerConstants.C_IM, "lz_customer");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_AVATAR, "http://lzan13.qiniudn.com/image/lz_bp_blue.png");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_TRUENAME, "我");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_USERNICKNAME, "风中小裤衩");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_DESCRIPTION, "风中的裤衩，孤孤单单，迎风飘扬");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_QQ, "1565176197");
        MLSPUtil.put(mActivity, CustomerConstants.C_USER_KEY_EMAIL, "lzan13@easemob.com");

    }
}
