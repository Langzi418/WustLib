package com.xuzhipeng.wustlib.module.mylib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.common.util.PrefUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mInputUser;
    private EditText mInputPassword;
    private AppCompatButton mBtnLogin;

    private MyLibHttp mLib;
    private TextView mLoginFailTv;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mInputUser = (EditText) findViewById(R.id.input_email);
        mInputPassword = (EditText) findViewById(R.id.input_password);
        mBtnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        mLoginFailTv = (TextView) findViewById(R.id.login_fail_tv);
    }

    @Override
    protected void setListener() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mLib = MyLibHttp.getInstance(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final String name = mInputUser.getText().toString();
                final String pwd = mInputPassword.getText().toString();
                getCheck(name, pwd);
                break;
        }
    }

    /**
     * 登陆验证
     */
    private void getCheck(final String name, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String info = mLib.checkLogin(name, pwd);

                Document doc = Jsoup.parse(info);
                Elements ele = doc.select("div#container div#left_tab form");

                if (ele.size() == 0) {
                    //已不在登陆界面,可能登录成功，也可能需要认证

                    PrefUtil.setUserNo(LoginActivity.this, name);
                    PrefUtil.setPwd(LoginActivity.this, pwd);

                    Elements readCon = doc.select("div.mylib_con_con");
                    if (readCon.size() != 0) {
//                        //需要认证
//                        startActivity(ReaderConActivity.
//                               newIntent(LoginActivity.this, info));
                        finish();
                        return;
                    }

                    //不需要认证
                    startActivity(MyLibActivity.newIntent(LoginActivity.this, info));
                    PrefUtil.setSuccess(LoginActivity.this, true);
                    finish();
                } else {
                    final Elements trs = ele.select("tr");  //登陆失败信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoginFailTv.setText(trs.last().text());
                        }
                    });
                }
            }
        }).start();
    }

}
