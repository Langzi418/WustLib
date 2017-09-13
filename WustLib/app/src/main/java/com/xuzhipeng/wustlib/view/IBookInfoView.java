package com.xuzhipeng.wustlib.view;

import com.xuzhipeng.wustlib.db.Comment;
import com.xuzhipeng.wustlib.model.DouBanInfo;
import com.xuzhipeng.wustlib.model.LibInfo;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IBookInfoView extends ILoadView {
    void setLibInfo(LibInfo libInfo);
    void setDouBanInfo(DouBanInfo douBanInfo);
    void setComments(List<Comment> comments);
}
