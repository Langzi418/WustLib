package com.xuzhipeng.wustlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.wustlib.common.util.PrefUtil;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.model.Search;
import com.xuzhipeng.wustlib.module.collection.CollectActivity;
import com.xuzhipeng.wustlib.module.home.HotBookFragment;
import com.xuzhipeng.wustlib.module.home.HotSearchFragment;
import com.xuzhipeng.wustlib.module.home.NoticeFragment;
import com.xuzhipeng.wustlib.module.intro.BookIntroActivity;
import com.xuzhipeng.wustlib.module.mylib.LoginActivity;
import com.xuzhipeng.wustlib.module.mylib.MyLibActivity;

public class MainActivity extends BaseActivity  {
    private static final String TAG = "MainActivity";
    private FloatingSearchView mSearchView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private TextView mUserTv;
    private Spinner mSearchTypeSpinner;
    private Spinner mDocTypeSpinner;
    private Spinner mDisplaySpinner;
    private Spinner mSortSpinner;
    private Spinner mAscDesSpinner;

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

        ViewUtil.setViewPager(adapter, this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavView.getHeaderView(0);
        mUserTv = (TextView) headerView.findViewById(R.id.user_info_tv);

        mSearchTypeSpinner = (Spinner) findViewById(R.id.search_type_spinner);
        mDocTypeSpinner = (Spinner) findViewById(R.id.doc_type_spinner);
        mDisplaySpinner = (Spinner) findViewById(R.id.display_spinner);
        mSortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        mAscDesSpinner = (Spinner) findViewById(R.id.asc_or_des_spinner);
    }

    @Override
    protected void setView() {
        setSpinner(this,mSearchTypeSpinner,R.array.searchType);
        setSpinner(this,mDocTypeSpinner,R.array.docType);
        setSpinner(this,mDisplaySpinner,R.array.display);
        setSpinner(this,mSortSpinner,R.array.sort);
        setSpinner(this,mAscDesSpinner,R.array.asc_des);
    }

    @Override
    protected void setListener() {

        /**
         *  floatingSearchView 左边点击
         */
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                mDrawerLayout.openDrawer(Gravity.START);
            }

            @Override
            public void onMenuClosed() {
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Search search = new Search();
                search.setStrText(currentQuery);
                search.setStrSearchType(mSearchTypeSpinner.getSelectedItemId());
                search.setDoctype(mDocTypeSpinner.getSelectedItemId());
                search.setDisplaypg(mDisplaySpinner.getSelectedItemId());
                search.setSort(mSortSpinner.getSelectedItemId());
                search.setOrderby(mAscDesSpinner.getSelectedItemId());
                startActivity(BookIntroActivity.newIntent
                        (MainActivity.this, search.toString(), search.getDisplaypg()));
            }
        });

        /**
         *  menu 点击
         */
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        mDrawerLayout.openDrawer(Gravity.END);
                        break;
                    case R.id.nav_change:
                        //登录状态设为false
                        PrefUtil.setSuccess(MainActivity.this,false);
                        startActivity(LoginActivity.newIntent(MainActivity.this));
                        break;
                    case R.id.nav_mylib:
                        goIfLogin();
                        break;
                    case R.id.nav_collection:
                        startActivity(CollectActivity.newIntent(MainActivity.this));
                        break;
                }

                return false;
            }
        });


        /**
         *  用户信息点击
         */
        mUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goIfLogin();
            }
        });
    }

    /**
     *  根据是否登录跳转
     */
    private void goIfLogin() {
        if(PrefUtil.getSuccess(MainActivity.this)){
            startActivity(MyLibActivity.newIntent(MainActivity.this,null));
        }else {
            startActivity(LoginActivity.newIntent(MainActivity.this));
        }
    }


    /**
     *  resume 刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(PrefUtil.getSuccess(this)){
            mUserTv.setText(PrefUtil.getUserName(this));
        }else {
            mUserTv.setText(R.string.click_login);
        }
    }


    /**
     * @param spinner 某个spinner
     * @param itemsId 对应的 数据 资源 id
     */
    public void setSpinner(Context context, Spinner spinner, int itemsId){
        String[] items = context.getResources().getStringArray(itemsId);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}
