package com.xuzhipeng.wustlib.presenter;

import android.util.Log;

import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.common.util.HttpUtil;
import com.xuzhipeng.wustlib.model.BookStatus;
import com.xuzhipeng.wustlib.model.DouBanInfo;
import com.xuzhipeng.wustlib.model.LibInfo;
import com.xuzhipeng.wustlib.net.api.ApiManager;
import com.xuzhipeng.wustlib.view.IBookInfoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class BookInfoPresenter extends BasePresenter<IBookInfoView> {

    private static final String TAG = "BookInfoPresenter";

    public BookInfoPresenter(IBookInfoView view) {
        attachView(view);
    }

    public void loadLibInfo(final String url) {
        Observable.create(new ObservableOnSubscribe<LibInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<LibInfo> e) throws Exception {
                Response response = HttpUtil.sendOkHttp(url);

                if (response.isSuccessful()) {
                    LibInfo libInfo = new LibInfo();
                    try {
                        Document doc = Jsoup.parse(response.body().string());
                        libInfo.setName(doc.select("div#item_detail dd").first().text());

                        Elements elements = doc.select("dl.booklist");
                        Elements bookStatusInfo = doc.select("table#item tbody tr");

                        for (int i = 2; i < elements.size(); i++) {
                            Elements dt = elements.get(i).select("dt");
                            String dtText = dt.text();
                            if (dtText.equals("ISBN及定价:")) {
                                libInfo.setIsbn(elements.get(i).select("dd").text().split(" |/")
                                        [0]); //" "或"/" 分割
                            } else if (dtText.equals("中图法分类号:")) {
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
                        libInfo.setStatusList(statusList);

                        e.onNext(libInfo);
                        e.onComplete();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LibInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull LibInfo info) {
                        mView.setLibInfo(info);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void loadDoubanInfo(String url) {
        ApiManager.getBookInfoApi()
                .getDouBanInfo(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouBanInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DouBanInfo douBanInfo) {
                        mView.setDouBanInfo(douBanInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.setDouBanInfo(null);
                        if(BuildConfig.DEBUG) Log.d(TAG, "onError: ",e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


}
