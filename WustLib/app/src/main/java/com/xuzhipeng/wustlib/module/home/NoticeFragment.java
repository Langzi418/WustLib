package com.xuzhipeng.wustlib.module.home;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.LazyLoadFragment;
import com.xuzhipeng.wustlib.common.util.HttpUtil;
import com.xuzhipeng.wustlib.common.util.NetWorkUtil;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.model.Notice;
import com.xuzhipeng.wustlib.module.adapter.NoticeAdapter;

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
import okhttp3.Response;


public class NoticeFragment extends LazyLoadFragment {

    private static final String TAG = "NoticeFragment";

    private static final String homeUrl = "http://www.lib.wust.edu.cn/";
    private RecyclerView mNoticeRv;
    private NoticeAdapter mNoticeAdapter;
    private List<Notice> mNotices;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initView(View view) {
        mNoticeRv = (RecyclerView) view.findViewById(R.id.notice_rv);
    }

    @Override
    protected void setView() {

        mNoticeAdapter = new NoticeAdapter(R.layout.item_notice, null);
        mNoticeRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoticeRv.setAdapter(mNoticeAdapter);

        ViewUtil.setRvDivider(mNoticeRv);


    }

    @Override
    protected void setListener() {
        mNoticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(NoticeActivity.newIntent(getActivity(),
                        homeUrl + mNotices.get(position).getUrl()));
            }
        });
    }

    @Override
    protected void loadData() {
        if(!NetWorkUtil.isNetworkConnected(getActivity())){
            return;
        }
        super.loadData();
        getData();
    }

    private void getData() {
        Observable.create(new ObservableOnSubscribe<List<Notice>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Notice>> e) throws Exception {
                List<Notice> notices = new ArrayList<>();
                Response response = HttpUtil.sendOkHttp(homeUrl);
                Document doc = Jsoup.parse(response.body().string());
                Elements elements = doc.select("table#gonggao td");
                for (Element el : elements) {
                    Notice notice = new Notice();
                    Elements a = el.select("a");
                    notice.setUrl(a.attr("href"));
                    notice.setTitle(a.text());
                    notice.setDate(el.select("font").text());
                    notices.add(notice);
                }
                e.onNext(notices);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Notice>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        NoticeFragment.super.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull List<Notice> notices) {
                        mNotices = notices;
                        mNoticeAdapter.setNewData(mNotices);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        NoticeFragment.super.hideProgress();
                        if (BuildConfig.DEBUG)
                            Log.d(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        NoticeFragment.super.hideProgress();
                    }
                });
    }


}
