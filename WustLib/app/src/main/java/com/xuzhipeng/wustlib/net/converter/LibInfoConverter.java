package com.xuzhipeng.wustlib.net.converter;

import com.xuzhipeng.wustlib.model.BookStatus;
import com.xuzhipeng.wustlib.model.LibInfo;

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

public class LibInfoConverter implements Converter<ResponseBody,LibInfo> {

    @Override
    public LibInfo convert(ResponseBody value) throws IOException {

        LibInfo libInfo = new LibInfo();

        try {
            Document doc = Jsoup.parse(value.string());
            libInfo.setName(doc.select("div#item_detail dd").first().text());

            Elements elements = doc.select("dl.booklist");
            Elements bookStatusInfo = doc.select("table#item tbody tr");

            for (int i = 2; i < elements.size(); i++) {
                Elements dt = elements.get(i).select("dt");
                String dtText = dt.text();
                if (dtText.equals("ISBN及定价:")) {
                    libInfo.setIsbn(elements.get(i).select("dd").text().split(" |/")[0]); //" "或"/" 分割
                }else if(dtText.equals("中图法分类号:")){
                    libInfo.setCategory(elements.get(i).select("dd").text());
                    break;
                }
            }

            List<BookStatus> statusList = new ArrayList<>();
            for (int i = 1; i < bookStatusInfo.size(); i++) {
                Elements books = bookStatusInfo.get(i).select("td");
                if (books.size() >= 5) { //正常状态
                    BookStatus bs = new BookStatus();
                    bs.setIndex(books.get(0).text());
                    bs.setPlace(books.get(3).text());
                    bs.setStatus(books.get(4).text());
                    statusList.add(bs);
                }
            }

//            libInfo.setStatusList(statusList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
