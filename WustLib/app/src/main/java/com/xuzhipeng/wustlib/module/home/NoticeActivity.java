package com.xuzhipeng.wustlib.module.home;

import android.content.Context;
import android.content.Intent;

import com.xuzhipeng.wustlib.base.BaseWebActivity;

public class NoticeActivity extends BaseWebActivity {


    private static final String ARGS_NOTICE_URL = "NOTICE_URL";

    private String mItemUrl;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, NoticeActivity.class);
        intent.putExtra(ARGS_NOTICE_URL, url);
        return intent;
    }



    @Override
    public String getUrl() {
        mItemUrl = getIntent().getStringExtra(ARGS_NOTICE_URL);
        return mItemUrl;
    }
}
