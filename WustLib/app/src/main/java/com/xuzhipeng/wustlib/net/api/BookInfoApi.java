package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.model.DouBanInfo;
import com.xuzhipeng.wustlib.model.LibInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface BookInfoApi {
    @GET
    Observable<LibInfo> getLibInfo(@Url String path);

    @GET
    Observable<DouBanInfo> getDouBanInfo(@Url String url);
}
