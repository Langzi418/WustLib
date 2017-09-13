package com.xuzhipeng.wustlib.net;

import android.content.Context;

import com.xuzhipeng.wustlib.common.app.App;
import com.xuzhipeng.wustlib.common.util.NetWorkUtil;
import com.xuzhipeng.wustlib.net.converter.JsoupConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/29
 */

public class RetrofitClient {

    private static final String CACHE_HOME = "retrofitCache";
    public static final String URL_BASE = "http://opac.lib.wust.edu.cn:8080/opac/";
    private static Retrofit retrofit;


    public static Retrofit getInstance() {

        if (retrofit == null) {   //Single Check
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {   //Double Check
                    retrofit = create(App.getContext());
                }
            }
        }
        return retrofit;
    }

    private static Retrofit create(final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // log用拦截器
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();

        File cacheFile = new File(
                context.getExternalCacheDir(), CACHE_HOME);

        final Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!NetWorkUtil.isNetworkConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                if (NetWorkUtil.isNetworkConnected(context)) {
                    int maxAge = 0;
                    response.newBuilder()
                            .header("Cache-Control", "public,max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 30 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };

        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(cacheInterceptor)
                .cache(cache)
                .connectTimeout(15, TimeUnit.SECONDS)//设置超时
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//错误重连

        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(new JsoupConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}
