package com.easemob.easeui.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.easemob.chat.EMChatManager;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerHelper;

public class SplashActivity extends BaseActivity {

    private int mTime = 2000;
    private int mDurationTime = 1500;

    private View mRootView;

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
        mRootView = findViewById(R.id.layout_root);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(mDurationTime);
        mRootView.startAnimation(animation);
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
}
