package com.xuzhipeng.wustlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.base.MyFragmentPagerAdapter;
import com.xuzhipeng.wustlib.common.util.PrefUtil;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.db.DBUtil;
import com.xuzhipeng.wustlib.model.Search;
import com.xuzhipeng.wustlib.module.collection.CollectActivity;
import com.xuzhipeng.wustlib.module.home.HotBookFragment;
import com.xuzhipeng.wustlib.module.home.HotSearchFragment;
import com.xuzhipeng.wustlib.module.home.NoticeFragment;
import com.xuzhipeng.wustlib.module.intro.BookIntroActivity;
import com.xuzhipeng.wustlib.module.mylib.LoginActivity;
import com.xuzhipeng.wustlib.module.mylib.MyLibActivity;
import com.xuzhipeng.wustlib.module.update.UpdateActivity;
import com.xuzhipeng.wustlib.module.update.UpdateUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private MaterialSearchView mSearchView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private TextView mUserTv;
    private Spinner mSearchTypeSpinner;
    private Spinner mDocTypeSpinner;
    private Spinner mDisplaySpinner;
    private Spinner mSortSpinner;
    private Spinner mAscDesSpinner;
    private String mQuery;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //检测更新
        UpdateUtil.update(this);

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
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
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
        setToolbar(R.string.app_name, R.drawable.ic_menu);
        mSearchView.setHint(getString(R.string.please_search));
        setSpinner(this, mSearchTypeSpinner, R.array.searchType);
        setSpinner(this, mDocTypeSpinner, R.array.docType);
        setSpinner(this, mDisplaySpinner, R.array.display);
        setSpinner(this, mSortSpinner, R.array.sort);
        setSpinner(this, mAscDesSpinner, R.array.asc_des);
    }

    @Override
    protected void setListener() {
        /**
         *  搜索点击设置
         */
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, R.string.no_content, Toast.LENGTH_SHORT).show();
                    return true;
                }
                mQuery = query;
                goSearch(mQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSuggest(newText);
                return false;
            }
        });



        /**
         *  menu 点击
         */
        mNavView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        mDrawerLayout.openDrawer(Gravity.END);
                        break;
                    case R.id.nav_change:
                        startActivity(LoginActivity.newIntent(MainActivity.this));
                        break;
                    case R.id.nav_mylib:
                        goIfLogin();
                        break;
                    case R.id.nav_collection:
                        startActivity(CollectActivity.newIntent(MainActivity.this));
                        break;
                    case R.id.nav_about:
                        startActivity(UpdateActivity.newIntent(MainActivity.this));
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
     * 执行 search
     */
    private void goSearch(String query) {
        Search search = new Search();
        search.setStrText(query);
        search.setStrSearchType(mSearchTypeSpinner.getSelectedItemId());
        search.setDoctype(mDocTypeSpinner.getSelectedItemId());
        search.setDisplaypg(mDisplaySpinner.getSelectedItemId());
        search.setSort(mSortSpinner.getSelectedItemId());
        search.setOrderby(mAscDesSpinner.getSelectedItemId());
        startActivity(BookIntroActivity.newIntent
                (MainActivity.this, search.toString(), search.getDisplaypg()));
    }

    /**
     * 从数据库中得到数据
     */
    private void getSuggest(String newText) {
        Log.d(TAG, "getSuggest: ");
        final String[] strings = DBUtil.querySuggestLike(newText);
        Log.d(TAG, "strings: "+strings);
        if (strings != null) {
            Log.d(TAG, "getSuggest: ");
            mSearchView.setSuggestions(strings);
            for (int i = 0; i < strings.length; i++) {
                Log.d(TAG, "getSuggest: " + strings[i]);
            }
            mSearchView.showSuggestions();
            mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        goSearch((String) parent.getItemAtPosition(position));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /**
     * 根据是否登录跳转
     */
    private void goIfLogin() {
        if (PrefUtil.getSuccess(MainActivity.this)) {
            startActivity(MyLibActivity.newIntent(MainActivity.this, null));
        } else {
            startActivity(LoginActivity.newIntent(MainActivity.this));
        }
    }


    /**
     * resume 刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (PrefUtil.getSuccess(this)) {
            mUserTv.setText(PrefUtil.getUserName(this));
        } else {
            mUserTv.setText(R.string.click_login);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!TextUtils.isEmpty(mQuery)) {
            DBUtil.insertSuggest(mQuery);
            DBUtil.closeDB();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param spinner 某个spinner
     * @param itemsId 对应的 数据 资源 id
     */
    public void setSpinner(Context context, Spinner spinner, int itemsId) {
        String[] items = context.getResources().getStringArray(itemsId);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}
