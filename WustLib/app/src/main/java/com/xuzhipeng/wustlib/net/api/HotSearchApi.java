package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.model.HotSearch;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface HotSearchApi {
    @GET("{path}")
    Observable<List<HotSearch>> getHotSearch(@Path("path") String path);
}
