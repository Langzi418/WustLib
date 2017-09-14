package com.xuzhipeng.wustlib.module.mylib;

import android.content.Context;
import android.net.Uri;

import com.xuzhipeng.wustlib.common.util.PrefUtil;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/17
 */

public class MyLibHttp {

    private static final String TAG = "MyLibHttp";

    private static final String URL_CHECK = "http://opac.lib.wust.edu.cn:8080/reader/redr_verify.php";
    private static final String URL_DQJY = "http://opac.lib.wust.edu.cn:8080/reader/book_lst.php";
    private static final String URL_JYLS = "http://opac.lib.wust.edu.cn:8080/reader/book_hist.php";
    private static final String URL_RENEW = "http://opac.lib.wust.edu.cn:8080/reader/ajax_renew.php?";
    private static final String URL_CON_RESULT = "http://opac.lib.wust.edu.cn:8080/reader/redr_con_result.php";

    /**
     *  mClient 自动持久化 存储管理 cookie , 单例模式
     */
    private volatile static MyLibHttp sLib;
    private OkHttpClient mClient;
    private Context mContext;


    private MyLibHttp(Context context){

        mContext = context;

        CookieJarImpl cookieJar = new CookieJarImpl(
                new PersistentCookieStore(context.getApplicationContext()));

        mClient = new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .build();

    }


    public static MyLibHttp getInstance(Context context) {
        if (sLib == null) {   //Single Check
            synchronized (MyLibHttp.class) {
                if (sLib == null) {   //Double Check
                    sLib = new MyLibHttp(context);
                }
            }
        }
        return sLib;
    }


    private Response sendOkReq(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return mClient.newCall(request).execute();
    }

    private String getHtml(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public String checkLogin(String username, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("number", username)
                .add("passwd", password)
                .add("select", "bar_no")
                .build();

        Request request = new Request.Builder()
                .url(URL_CHECK)
                .post(requestBody)
                .build();

        try {

            Response response = mClient.newCall(request).execute();

            if (response.isSuccessful()) {
                String htmlCon = getHtml(response.body().byteStream());

                return htmlCon;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public boolean getConRes(String name){
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .build();

        Request request = new Request.Builder()
                .url(URL_CON_RESULT)
                .post(requestBody)
                .build();


        try {
            Response response = mClient.newCall(request).execute();

            if (response.isSuccessful()) {
                String conRes = getHtml(response.body().byteStream());
                Document doc = Jsoup.parse(conRes);
                Elements ele = doc.select("div#container div#navsidebar");

                if(ele.size()!=0){
                    return true;
                }else {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    public String loadZJXX() {

        String username = PrefUtil.getUserNo(mContext);
        String pwd = PrefUtil.getPwd(mContext);

        if (username != null && pwd != null) {
            return checkLogin(username,pwd);
        }

        return null;

    }


    public String loadDQJY(){
        try {
            Response response = sendOkReq(URL_DQJY);
            if(response.isSuccessful()){
                String html = getHtml(response.body().byteStream());
                return html;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String loadJYLS(int page){

        String url = Uri.parse(URL_JYLS)
                        .buildUpon()
                        .appendQueryParameter("page", String.valueOf(page))
                        .build().toString();
        try {
            Response response = sendOkReq(url);
            if(response.isSuccessful()){
                String html = getHtml(response.body().byteStream());
                return html;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String loadRenew(String barNo, String checkNo) {

        String url = Uri.parse(URL_RENEW).buildUpon()
                        .appendQueryParameter("bar_code",barNo)
                        .appendQueryParameter("check",checkNo)
                        .appendQueryParameter("time",String.valueOf(System.currentTimeMillis()))
                        .build().toString();


        try {
            Response response = sendOkReq(url);
            if(response.isSuccessful()){
                String html = getHtml(response.body().byteStream());
                Document doc = Jsoup.parse(html);
                String result = doc.getElementsByTag("font").text();
                if (result != null) {
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
