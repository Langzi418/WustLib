package com.xuzhipeng.wustlib.view;

import com.xuzhipeng.wustlib.model.BookIntro;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IBookIntroView extends ILoadView{
    void setResult(String result);
    void setIntros(List<BookIntro> intros);
    void setPageNum(int pageNum);
}
