package com.xuzhipeng.wustlib.presenter;

import com.xuzhipeng.wustlib.model.BookIntro;
import com.xuzhipeng.wustlib.model.Result;
import com.xuzhipeng.wustlib.net.api.ApiManager;
import com.xuzhipeng.wustlib.view.IBookIntroView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

public class BookIntroPresenter extends BasePresenter<IBookIntroView> {

    public BookIntroPresenter(IBookIntroView view) {
        attachView(view);
    }

    public void loadBookIntros(String url){
        ApiManager.getIntroAPi()
                .getBookIntro(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookIntro>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<BookIntro> bookIntros) {
                        mView.setIntros(bookIntros);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }


    public void loadResult(String url){
        ApiManager.getIntroAPi()
                .getResult(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Result result) {
                        mView.setResult(result.getResult());
                        mView.setPageNum(result.getPageNum());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
