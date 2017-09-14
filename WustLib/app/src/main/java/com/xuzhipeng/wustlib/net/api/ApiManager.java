package com.xuzhipeng.wustlib.net.api;

import com.xuzhipeng.wustlib.net.RetrofitClient;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class ApiManager {

    public static final String BASE_DOU_BAN = "https://api.douban.com/v2/book/isbn/";

    private static HotSearchApi hotSearchApi;
    private static IntroAPi introAPi;
    private static DouBanInfoApi douBanInfoApi;

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

    public static DouBanInfoApi getDouBanInfoApi(){
        if(douBanInfoApi == null){
            douBanInfoApi = RetrofitClient.getInstance().create(DouBanInfoApi.class);
        }
        return douBanInfoApi;
    }



}
