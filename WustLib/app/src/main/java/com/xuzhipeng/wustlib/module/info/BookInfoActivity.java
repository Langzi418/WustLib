package com.xuzhipeng.wustlib.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.db.Comment;
import com.xuzhipeng.wustlib.model.DouBanInfo;
import com.xuzhipeng.wustlib.model.LibInfo;
import com.xuzhipeng.wustlib.module.adapter.BookStatusAdapter;
import com.xuzhipeng.wustlib.net.RetrofitClient;
import com.xuzhipeng.wustlib.presenter.BookInfoPresenter;
import com.xuzhipeng.wustlib.view.IBookInfoView;

import java.util.List;

public class BookInfoActivity extends BaseActivity implements IBookInfoView {


    private static final String TAG = "BookInfoActivity";

    private static final String BASE_DOU_BAN = "https://api.douban.com/v2/book/isbn/";
    private static final String ARGS_URL_BOOK = "URL_BOOK";

    private ImageView mBookBgIv;
    private ImageView mBookImageView;
    private CollapsingToolbarLayout mCollapsingTb;
    private NestedScrollView mNsv;
    private RecyclerView mBookStatusRv;
    private RecyclerView mBookCommentRv;
    private LinearLayout mCtrlLL;

    private BookStatusAdapter mStatusAdapter;
    private String mUrl;
    private BookInfoPresenter mPresenter;
    private Toolbar mToolbar;

    //control
    private boolean isBottomShow = true;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, BookInfoActivity.class);
        intent.putExtra(ARGS_URL_BOOK, url);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_info;
    }

    @Override
    protected void initView() {
        mBookBgIv = (ImageView) findViewById(R.id.book_img_bg);
        mBookImageView = (ImageView) findViewById(R.id.book_image_view);
        mCollapsingTb = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingTb.setCollapsedTitleTextAppearance(R.style.TextAppearance_16);
        mCollapsingTb.setExpandedTitleTextAppearance(R.style.TextAppearance_12);

        mBookStatusRv = (RecyclerView) findViewById(R.id.book_status_rv);
        mBookCommentRv = (RecyclerView) findViewById(R.id.book_comment_rv);

        mStatusAdapter = new BookStatusAdapter(R.layout.item_book_status, null, this);
        mBookStatusRv.setLayoutManager(new LinearLayoutManager(this));
        mBookStatusRv.setAdapter(mStatusAdapter);
        mBookStatusRv.setNestedScrollingEnabled(false);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mBookCommentRv.setNestedScrollingEnabled(false);

        setSupportActionBar(mToolbar);  //toolBar 的标准用法 作为 ActionBar显示，并启用返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        mNsv = (NestedScrollView) findViewById(R.id.info_nsv);
//        mCtrlLL = (LinearLayout) findViewById(R.id.control_ll);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getExtra() {
        mUrl = getIntent().getStringExtra(ARGS_URL_BOOK);
    }

    @Override
    protected void initData() {
        mPresenter = new BookInfoPresenter(this);

        mPresenter.loadLibInfo(RetrofitClient.URL_BASE + mUrl);
    }

    @Override
    public void setLibInfo(LibInfo libInfo) {
        mCollapsingTb.setTitle(libInfo.getName());

        mStatusAdapter.setNewData(libInfo.getStatusList());

        mPresenter.loadDoubanInfo(BASE_DOU_BAN + libInfo.getIsbn());
    }

    @Override
    public void setDouBanInfo(DouBanInfo douBanInfo) {

        String imgUrl = null;
        if(douBanInfo!=null){
            imgUrl = douBanInfo.getImages().getLarge();
        }

        ViewUtil.useGlideUrl(this, imgUrl, mBookImageView);
        ViewUtil.useGlideBlur(this, imgUrl, mBookBgIv);

        super.hideProgress();
    }

    @Override
    public void setComments(List<Comment> comments) {

    }

}
