package com.easemob.easeui.customer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Class ${FILE_NAME}
 * <p/>
 * Created by lzan13 on 2015/11/6 20:58.
 */
public class BaseActivity extends AppCompatActivity {

    public BaseActivity mActivity;

    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

    }
}
