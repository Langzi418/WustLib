package com.xuzhipeng.wustlib.module.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.db.Book;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/14
 * Desc:
 */

public class CollectAdapter extends BaseQuickAdapter<Book,BaseViewHolder> {

    private Context mContext;

    public CollectAdapter(@LayoutRes int layoutResId, @Nullable List<Book> data,
                          Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Book item) {
        helper.setText(R.id.category_tv,item.getCategory())
        .setText(R.id.clt_title_tv,item.getName());

        ViewUtil.useGlideUrl(mContext,item.getImgUrl(),
                (ImageView) helper.getView(R.id.clt_iv));
    }
}
