package com.xuzhipeng.wustlib.net.converter;

import com.xuzhipeng.wustlib.model.HotSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class HotSearchConverter implements Converter<ResponseBody, List<HotSearch>> {

    @Override
    public List<HotSearch> convert(ResponseBody value) throws IOException {

        List<HotSearch> hotSearches = new ArrayList<>();
        try {

            Document doc = Jsoup.parse(value.string());
            Elements elements = doc.select("tbody a");  //解析<tbody>所有<a>
            for (int i = 0; i < 20; i++) {
                HotSearch hotKey = new HotSearch();
                hotKey.setText(elements.get(i).text());
                hotKey.setUrl(elements.get(i).attr("href"));

                hotSearches.add(hotKey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return hotSearches;

    }
}
