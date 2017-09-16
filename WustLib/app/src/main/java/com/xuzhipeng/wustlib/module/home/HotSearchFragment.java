package com.xuzhipeng.wustlib.module.home;


import android.support.v4.app.Fragment;
import android.view.View;

import com.moxun.tagcloudlib.view.TagCloudView;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.LazyLoadFragment;
import com.xuzhipeng.wustlib.model.HotSearch;
import com.xuzhipeng.wustlib.module.adapter.HotSearchAdapter;
import com.xuzhipeng.wustlib.presenter.HotSearchPresenter;
import com.xuzhipeng.wustlib.view.IHotSearchView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotSearchFragment extends LazyLoadFragment implements IHotSearchView {

    private static final String URL_HOT_SEARCH = "top100.php";

    private HotSearchAdapter mAdapter;
    private HotSearchPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_search;
    }

    @Override
    protected void initView(View view) {
        TagCloudView tagView = (TagCloudView) view.findViewById(R.id.hot_search_tcv);
        mAdapter = new HotSearchAdapter(getActivity());
        tagView.setAdapter(mAdapter);

        mPresenter = new HotSearchPresenter(this);

    }



    @Override
    protected void loadData() {
        super.loadData();

        mPresenter.loadHotSearch(URL_HOT_SEARCH);
    }

    @Override
    public void setHotSearch(List<HotSearch> searches) {
        mAdapter.setNewData(searches);
    }
}
