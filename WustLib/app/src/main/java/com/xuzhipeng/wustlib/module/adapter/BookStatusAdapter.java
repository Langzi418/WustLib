package com.xuzhipeng.wustlib.module.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.model.BookStatus;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class BookStatusAdapter extends BaseQuickAdapter<BookStatus, BaseViewHolder> {

    private Context mContext;

    public BookStatusAdapter(@LayoutRes int layoutResId, @Nullable List<BookStatus> data,
                             Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BookStatus item) {
        helper.setText(R.id.book_index_tv, item.getIndex())
                .setText(R.id.book_place_tv, item.getPlace())
                .setText(R.id.book_status_tv, item.getStatus());


        if (item.getStatus().equals("可借")) {
            helper.setTextColor(R.id.book_status_tv,
                    ContextCompat.getColor(mContext, R.color.green_light));
        } else {
            helper.setTextColor(R.id.book_status_tv,
                    ContextCompat.getColor(mContext, R.color.secondary_text_dark));
        }
    }
}
