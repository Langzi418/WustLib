package com.xuzhipeng.wustlib.net.converter;

import com.xuzhipeng.wustlib.model.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class ResultConverter implements Converter<ResponseBody,Result> {

    @Override
    public Result convert(ResponseBody value) throws IOException {

        Result result = new Result();

        try {
            Document doc = Jsoup.parse(value.string());
            Element element = doc.select("div.book_article p").first();
            if (element == null) {
                return null;
            }

            result.setResult(element.text());
            result.setPageNum(Integer.valueOf(element.select("strong.red").text()));

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
