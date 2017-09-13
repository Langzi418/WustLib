package com.xuzhipeng.wustlib.net.converter;

import android.support.annotation.Nullable;

import com.xuzhipeng.wustlib.model.BookIntro;
import com.xuzhipeng.wustlib.model.HotSearch;
import com.xuzhipeng.wustlib.model.LibInfo;
import com.xuzhipeng.wustlib.model.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc: 自定义 converter
 */

public class JsoupConverterFactory extends Converter.Factory{

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];

            if (rawType == List.class) {
                if (actualType == HotSearch.class) {
                    return new HotSearchConverter();
                }else if(actualType == BookIntro.class){
                    return new BookIntroConverter();
                }
            }
        }else if(type == Result.class){
            return new ResultConverter();
        }else if(type == LibInfo.class){
            return new LibInfoConverter();
        }

        return null;
    }
}
