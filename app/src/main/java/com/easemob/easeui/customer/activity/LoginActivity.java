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
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.util.MLLog;
import com.easemob.easeui.customer.util.MLSPUtil;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {


    // 登录进度dialog
    private ProgressDialog mDialog;

    // UI控件
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSigninButton;
    private View mProgressView;
    private View mLoginFormView;

    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.layout_login_form);
        mProgressView = findViewById(R.id.login_progress);
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
     *
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
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

    private void loginEasemob() {
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(getString(R.string.prompt_sign_in_begin));
        mDialog.show();
        EMChatManager.getInstance().login(mUsername, mPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                mDialog.dismiss();
                MLSPUtil.put(mActivity, "username", mUsername);
                MLSPUtil.put(mActivity, "password", mPassword);
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

