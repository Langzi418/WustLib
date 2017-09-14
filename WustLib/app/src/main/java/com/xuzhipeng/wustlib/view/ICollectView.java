package com.xuzhipeng.wustlib.view;

import com.xuzhipeng.wustlib.db.Book;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/14
 * Desc:
 */

public interface ICollectView extends ILoadView{
    void setBooks(List<Book> books);
}
