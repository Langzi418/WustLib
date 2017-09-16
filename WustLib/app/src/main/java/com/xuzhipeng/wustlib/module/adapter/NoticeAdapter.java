package com.xuzhipeng.wustlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.model.Notice;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/15
 * Desc:
 */

public class NoticeAdapter extends BaseQuickAdapter<Notice, BaseViewHolder> {



    public NoticeAdapter(@LayoutRes int layoutResId, @Nullable List<Notice> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, Notice item) {
        helper.setText(R.id.ntc_title_tv, item.getTitle())
                .setText(R.id.ntc_date_tv, item.getDate());



    }
}
