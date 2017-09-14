package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.model.DouBanInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc:
 */

public interface DouBanInfoApi {
    @GET
    Observable<DouBanInfo> getDouBanInfo(@Url String utl);
}
