package com.xuzhipeng.wustlib.presenter;

import com.xuzhipeng.wustlib.model.HotSearch;
import com.xuzhipeng.wustlib.net.api.ApiManager;
import com.xuzhipeng.wustlib.view.IHotSearchView;

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

public class HotSearchPresenter extends BasePresenter<IHotSearchView> {

    public HotSearchPresenter(IHotSearchView view) {
        attachView(view);
    }

    public void loadHotSearch(String path){
        ApiManager.getHotSearchApi()
                .getHotSearch(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HotSearch>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<HotSearch> hotSearches) {
                        mView.setHotSearch(hotSearches);
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
}
