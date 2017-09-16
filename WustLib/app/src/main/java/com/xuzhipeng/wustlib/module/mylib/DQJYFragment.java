package com.xuzhipeng.wustlib.module.mylib;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.LazyLoadFragment;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.model.DQJY;
import com.xuzhipeng.wustlib.module.adapter.DQJYAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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


public class DQJYFragment extends LazyLoadFragment {
    private static final String TAG = "DQJYFragment";

    private RecyclerView mDqjyRv;
    private MyLibHttp mLib;
    private DQJYAdapter mDQJYAdapter;
    private List<DQJY> mDQJYs;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dqjy;
    }

    @Override
    protected void initView(View view) {
        mDqjyRv = (RecyclerView) view.findViewById(R.id.dqjy_rv);
    }

    @Override
    protected void setView() {
        mDQJYAdapter = new DQJYAdapter(R.layout.item_dqjy, null);
        mDqjyRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDqjyRv.setAdapter(mDQJYAdapter);
        ViewUtil.setRvDivider(mDqjyRv);
    }

    @Override
    protected void setListener() {
        mDQJYAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.renew_button:
                        handleRenew(position);
                        break;
                }
            }
        });
    }

    /**
     * 处理续借
     */
    private void handleRenew(final int position) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                DQJY dqjy = mDQJYs.get(position);
                String result = mLib.loadRenew(dqjy.getBarNo(), dqjy.getCheckNo());
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        ViewUtil.showDialog(getActivity(), getString(R.string.renew_hint, s));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "renewError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void initData() {
        mLib = MyLibHttp.getInstance(getActivity());
    }

    @Override
    protected void loadData() {
        super.loadData();
        Observable.create(new ObservableOnSubscribe<List<DQJY>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DQJY>> e) throws Exception {
                String dqjyHtml = mLib.loadDQJY();
                if (dqjyHtml == null) {
                    return;
                }
                List<DQJY> dqjyList = new ArrayList<>();

                Document doc = Jsoup.parse(dqjyHtml);
                Elements trs = doc.select("div#mylib_content table tr");
                for (int i = 1; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    Elements tds = tr.getElementsByTag("td");
                    DQJY dqjy = new DQJY();

                    dqjy.setBarNo(tds.get(0).text());
                    dqjy.setBookName(tds.get(1).text());
                    dqjy.setBookJHX("借：" + tds.get(2).text() + "     还：" + tds.get(3).text() +
                            "     续：" + tds.get(4).text());
                    dqjy.setBookGCD(tds.get(5).text());

                    Elements input = tr.select("input");
                    dqjy.setCheckNo(input.attr("onclick").substring(21, 29));

                    dqjyList.add(dqjy);
                }
                e.onNext(dqjyList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DQJY>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        DQJYFragment.super.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<DQJY> dqjies) {
                        mDQJYs = dqjies;
                        mDQJYAdapter.setNewData(mDQJYs);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DQJYFragment.super.hideProgress();
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "dqjyError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        DQJYFragment.super.hideProgress();
                    }
                });
    }
}
