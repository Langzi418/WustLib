package com.xuzhipeng.wustlib.module.mylib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.wustlib.common.util.ViewUtil;


public class MyLibActivity extends BaseActivity {

    private static final String EXTRA_HTML = "HTML";
    private static final String BUNDLE_HTML = "html";

    public static Intent newIntent(Context context, String htmlCon) {
        Intent intent = new Intent(context, MyLibActivity.class);
        intent.putExtra(EXTRA_HTML, htmlCon);
        return intent;
    }


    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
    //        return true;
    //    }
    //
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        switch (item.getItemId()) {
    //            case R.id.login_out:
    //                Preferences.setUser(this,null); //清空用户信息
    //                Preferences.setPwd(this,null);
    //                Preferences.setSuccess(this,false); //不在登陆状态
    //                startActivity(LoginActivity.newIntent(this));
    //                finish(); //摧毁活动
    //                break;
    //            default:
    //        }
    //        return super.onOptionsItemSelected(item);
    //    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_lib;
    }

    @Override
    protected void initView() {

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_HTML, getIntent().getStringExtra(EXTRA_HTML));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.zjxx, ZJXXFragment.class, bundle)
                        .add(R.string.dqjy, DQJYFragment.class)
                        .create()
        );

        ViewUtil.setViewPager(adapter, this);
    }

    @Override
    protected void setView() {
        setToolbar(R.string.my_lib,null);
    }
}
