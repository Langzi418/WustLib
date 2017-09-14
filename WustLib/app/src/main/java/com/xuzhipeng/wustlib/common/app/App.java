package com.xuzhipeng.wustlib.common.app;

import android.app.Application;
import android.content.Context;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/29
 */

public class App extends Application {


    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    public static Context getContext(){
        return sContext;
    }

}
