package com.xuzhipeng.wustlib.common.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/17
 */

public class PrefUtil {

    //是否登录成功
    private static final String PREF_SUCCESS = "success";

    //用于登录
    private static final String PREF_USER_NO = "userNo";
    private static final String PREF_PASSWORD = "password";

    private static final String PREF_USER_NAME = "username";

    //用于查找用户收藏资料
    private static final String PREF_USER_ID = "userId";


    public static String getUserNo(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_NO,null);
    }

    public static void setUserNo(Context context, String userNo){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_NO,userNo)
                .apply();
    }

    public static String getPwd(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_PASSWORD,null);
    }

    public static void setPwd(Context context,String pwd){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_PASSWORD,pwd)
                .apply();
    }

    public static boolean getSuccess(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_SUCCESS,false);
    }

    public static void setSuccess(Context context,boolean login){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_SUCCESS,login)
                .apply();
    }

    public static Long getUserId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_USER_ID,0L);
    }

    public static void setUserId(Context context,Long userId){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(PREF_USER_ID,userId)
                .apply();
    }

    public static String getUserName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_NAME,null);
    }

    public static void setUserName(Context context,String username){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_NAME,username)
                .apply();
    }

}
