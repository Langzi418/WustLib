package com.xuzhipeng.wustlib;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.model.Search;
import com.xuzhipeng.wustlib.module.home.HotBookFragment;
import com.xuzhipeng.wustlib.module.home.HotSearchFragment;
import com.xuzhipeng.wustlib.module.home.NoticeFragment;
import com.xuzhipeng.wustlib.module.intro.BookIntroActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FloatingSearchView mSearchView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.notice, NoticeFragment.class)
                        .add(R.string.hot_search, HotSearchFragment.class)
                        .add(R.string.hot_book, HotBookFragment.class)
                        .create()
        );

        ViewUtil.setViewPager(adapter,this);

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
    }

    @Override
    protected void setListener() {
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Search search = new Search();
                search.setDefault();
                search.setStrText(currentQuery);
                startActivity(BookIntroActivity.newIntent
                        (MainActivity.this,search.toString(),search.getDisplaypg()));
            }
        });
    }
}
