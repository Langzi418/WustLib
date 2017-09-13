package com.xuzhipeng.wustlib.net.converter;

import com.xuzhipeng.wustlib.model.BookIntro;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

public class BookIntroConverter implements Converter<ResponseBody,List<BookIntro>>{
    @Override
    public List<BookIntro> convert(ResponseBody value) throws IOException {

        List<BookIntro> intros = new ArrayList<>();

        try{
            Document doc = Jsoup.parse(value.string());
            Elements elements = doc.select("ol#search_book_list li");
            //逐条解析数据
            for (Element element : elements) {
                BookIntro intro = new BookIntro();

                String docType = element.select("h3 span").text(); //图书类型
                intro.setNameIndex(
                        element.select("h3").text().replace(docType, ""));//图书名

                intro.setInfoUrl(element.select("h3 a").attr("href"));

                //" "正则表达式处理得到其他信息
                String[] otherInfo = element.select("p").text().split(" ");
                intro.setStore(otherInfo[0]); //馆藏
                intro.setLoanable(otherInfo[1]); //可借
                intro.setPressYear(otherInfo[otherInfo.length - 3]);

                StringBuilder authorInfo = new StringBuilder();
                for (int i = 2; i < otherInfo.length - 3; i++) {
                    authorInfo.append(otherInfo[i]);
                    authorInfo.append(" ");
                }

                intro.setAuthor(authorInfo.toString());
                authorInfo.setLength(0);

                intros.add(intro); //添加
            }

            return intros;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
