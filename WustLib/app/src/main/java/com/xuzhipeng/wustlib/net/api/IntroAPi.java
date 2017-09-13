package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.model.BookIntro;
import com.xuzhipeng.wustlib.model.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public interface IntroAPi {
    @GET
    Observable<List<BookIntro>> getBookIntro(@Url String url);

    @GET
    Observable<Result> getResult(@Url String url);
}
