package com.xuzhipeng.wustlib.module.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.common.util.DateUtil;
import com.xuzhipeng.wustlib.db.Comment;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/15
 * Desc:
 */

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    public CommentAdapter(@LayoutRes int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        helper.setText(R.id.comment_username, item.getUsername())
                .setText(R.id.comment_date, DateUtil.formatDate(item.getDate()))
                .setText(R.id.comment_content, item.getContent());
    }

//    /**
//     *  头部插入 mData 父类
//     */
//    public void insertToFirst(Comment comment){
//        if(mData == null){
//            mData = new ArrayList<>();
//        }
//        mData.add(0,comment);
//        notif yDataSetChanged();
//    }

}
