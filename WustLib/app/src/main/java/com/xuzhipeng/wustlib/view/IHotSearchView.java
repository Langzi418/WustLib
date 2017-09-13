package com.xuzhipeng.wustlib.view;

import com.xuzhipeng.wustlib.model.HotSearch;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IHotSearchView extends ILoadView{
    void setHotSearch(List<HotSearch> searches);
}
