package com.xuzhipeng.wustlib.presenter;

import android.util.Log;

import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.db.Book;
import com.xuzhipeng.wustlib.db.DBUtil;
import com.xuzhipeng.wustlib.view.ICollectView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/14
 * Desc:
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    private static final String TAG = "CollectPresenter";

    public CollectPresenter(ICollectView view) {
        attachView(view);
    }


    public void loadBooks(final Long userId) {
        Observable.create(new ObservableOnSubscribe<List<Book>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Book>> e) throws Exception {

                List<Book> books = DBUtil.queryLikeBook(userId);
//                User user = DBUtil.queryUserById(userId);
//                if(user!=null) {
//                    user.resetBooks();
//                    List<Book> userBooks = user.getBooks();
//                    //遍历，加入
//                    for(Book book:userBooks){
//                        if(book.getLike()){
//                            books.add(book);
//                        }
//                    }
//                    if (BuildConfig.DEBUG)
//                        Log.d(TAG, "subscribe: " + books.size());
//                }

                DBUtil.closeDB();
                e.onNext(books);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Book>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<Book> books) {
                        mView.setBooks(books);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "onError: ", e);
                        mView.setBooks(null);
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });

    }

}
