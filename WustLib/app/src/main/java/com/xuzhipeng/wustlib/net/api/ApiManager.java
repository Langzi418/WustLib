package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.net.RetrofitClient;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class ApiManager {
    private static HotSearchApi hotSearchApi;
    private static IntroAPi introAPi;
    private static BookInfoApi bookInfoApi;

    public static HotSearchApi getHotSearchApi(){
        if(hotSearchApi == null){
            hotSearchApi = RetrofitClient.getInstance().create(HotSearchApi.class);
        }

        return hotSearchApi;
    }

    public static IntroAPi getIntroAPi(){
        if(introAPi == null){
            introAPi = RetrofitClient.getInstance().create(IntroAPi.class);
        }

        return introAPi;
    }

    public static BookInfoApi getBookInfoApi(){
        if(bookInfoApi == null){
            bookInfoApi = RetrofitClient.getInstance().create(BookInfoApi.class);
        }

        return bookInfoApi;
    }

}
