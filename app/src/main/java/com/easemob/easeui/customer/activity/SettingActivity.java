package com.easemob.easeui.customer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.util.MLSPUtil;

/**
 * Created by lzan13 on 2015/11/6 20:50.
 */
public class SettingActivity extends BaseActivity {

    private View mRootView;
    private TextView mAppkeyView;
    private TextView mIMCustomerView;
    private TextView mUserInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToolbar();
        initView();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);

        mToolbar.setTitle(R.string.title_activity_setting);
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
     * 初始化设置UI
     */
    private void initView() {
        mRootView = findViewById(R.id.layout_setting_root);
        mAppkeyView = (TextView) findViewById(R.id.text_setting_appkey);
        mIMCustomerView = (TextView) findViewById(R.id.text_setting_im_customer);
        mUserInfoView = (TextView) findViewById(R.id.text_setting_user_info);

        mAppkeyView.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_APPKEY, ""));
        mIMCustomerView.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_IM, ""));


        mAppkeyView.setOnClickListener(viewListener);
        mIMCustomerView.setOnClickListener(viewListener);
        mUserInfoView.setOnClickListener(viewListener);
    }


    /**
     * 设置界面点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_setting_appkey:
                    changeAppkey();
                    break;
                case R.id.text_setting_im_customer:
                    changeIMCustomer();
                    break;
                case R.id.text_setting_user_info:
                    changeUserInfo();
                    break;
            }
        }
    };

    /**
     * 改变Appkey
     */
    private void changeAppkey() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        final EditText editText = new EditText(mActivity);
        editText.setHint(R.string.dialog_title_change_appkey);
        editText.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_APPKEY, ""));
        dialog.setTitle(R.string.dialog_title_change_im_customer);
        dialog.setView(editText);
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 保存新设置的IM关联号
                MLSPUtil.put(mActivity, CustomerConstants.C_APPKEY, editText.getText().toString());
                Snackbar.make(mRootView, "Appkey保存成功，请重启app", Snackbar.LENGTH_LONG).show();

                // 更新UI显示的appkey
                mAppkeyView.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_APPKEY, ""));
//                System.exit(0);
            }
        });
        dialog.show();
    }

    /**
     * 改变客服关联账户
     */
    private void changeIMCustomer() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        final EditText editText = new EditText(mActivity);
        editText.setHint(R.string.btn_setting_im_customer);
        editText.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_IM, ""));
        dialog.setTitle(R.string.dialog_title_change_im_customer);
        dialog.setView(editText);
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 保存新设置的IM关联号
                MLSPUtil.put(mActivity, CustomerConstants.C_IM, editText.getText().toString());
                Snackbar.make(mRootView, "关联客服号保存成功", Snackbar.LENGTH_LONG).show();

                // 更新UI显示的IM关联号
                mIMCustomerView.setText((CharSequence) MLSPUtil.get(mActivity, CustomerConstants.C_IM, ""));
            }
        });
        dialog.show();
    }

    /**
     * 修改账户信息
     */
    private void changeUserInfo() {
        Snackbar.make(mRootView, "暂未实现修改用户信息界面", Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
