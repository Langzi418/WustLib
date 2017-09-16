package com.xuzhipeng.wustlib.common.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc:
 */

public class HttpUtil {
    public static Response sendOkHttp(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }
}
