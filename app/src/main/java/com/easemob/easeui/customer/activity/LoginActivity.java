package com.easemob.easeui.customer.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.util.MLLog;
import com.easemob.easeui.customer.util.MLSPUtil;


/**
 * Created by lzan13 on 2015/11/6 21:00.
 * 登陆界面，实现了环信账户的登录
 */
public class LoginActivity extends BaseActivity {


    // 登录进度dialog
    private ProgressDialog mDialog;

    // UI控件
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSigninButton;

    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.text_login_username);
        mPasswordView = (EditText) findViewById(R.id.text_login_password);

        mSigninButton = (Button) findViewById(R.id.button_sign_in);
        mSigninButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * 检测登陆，主要先判断是否满足登陆条件
     */
    private void attemptLogin() {

        // 重置错误
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        mUsername = mUsernameView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码是否为空
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // 检查username是否为空
        if (TextUtils.isEmpty(mUsername)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            loginEasemob();
        }
    }

    /**
     * 登陆到环信
     */
    private void loginEasemob() {
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(getString(R.string.prompt_sign_in_begin));
        mDialog.show();
        EMChatManager.getInstance().login(mUsername, mPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                mDialog.dismiss();
                // 登录成功保存登录信息
                MLSPUtil.put(mActivity, "username", mUsername);
                MLSPUtil.put(mActivity, "password", mPassword);

                // 加载所有会话到内存
                EMChatManager.getInstance().loadAllConversations();

                // 登录成功跳转到主界面
                Intent intent = new Intent();
                intent.setClass(mActivity, MainActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
            }

            @Override
            public void onError(int i, String s) {
                mDialog.dismiss();
                MLLog.e(s + i);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


}

