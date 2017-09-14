package com.xuzhipeng.wustlib.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.common.util.PrefUtil;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.db.Book;
import com.xuzhipeng.wustlib.db.Comment;
import com.xuzhipeng.wustlib.db.DBUtil;
import com.xuzhipeng.wustlib.model.DouBanInfo;
import com.xuzhipeng.wustlib.model.DouComment;
import com.xuzhipeng.wustlib.model.LibInfo;
import com.xuzhipeng.wustlib.module.adapter.BookStatusAdapter;
import com.xuzhipeng.wustlib.module.adapter.DouCmtAdapter;
import com.xuzhipeng.wustlib.net.RetrofitClient;
import com.xuzhipeng.wustlib.net.api.ApiManager;
import com.xuzhipeng.wustlib.presenter.BookInfoPresenter;
import com.xuzhipeng.wustlib.view.IBookInfoView;

import java.util.List;

public class BookInfoActivity extends BaseActivity implements IBookInfoView {

    private static final String TAG = "BookInfoActivity";

    public static final String ARGS_URL_BOOK = "URL_BOOK";
    public static final String ARGS_POS_BOOK = "POS_BOOK";

    private ImageView mBookBgIv;
    private ImageView mBookImageView;
    private CollapsingToolbarLayout mCollapsingTb;
    private RecyclerView mBookStatusRv;
    private RecyclerView mBookCommentRv;

    private BookStatusAdapter mStatusAdapter;
    private BookInfoPresenter mPresenter;

    private DouCmtAdapter mDouAdapter;
    private Toolbar mToolbar;
    private RecyclerView mDouCmtRv;
    private View mEmptyView;
    private LikeButton mLikeBtn;
    private ImageButton mCommentIb;

    //数据库数据
    private Book mBook;
    private String mIsbn;
    private String mName;
    private String mImgUrl;
    private String mInfoUrl;
    private String mCategory;

    //数据返回
    private int mPos;
    private boolean isBack;

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
        mBookStatusRv = (RecyclerView) findViewById(R.id.book_status_rv);
        mBookCommentRv = (RecyclerView) findViewById(R.id.book_comment_rv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDouCmtRv = (RecyclerView) findViewById(R.id.dou_comment_rv);

        mEmptyView = getLayoutInflater().inflate(
                R.layout.view_empty, (ViewGroup) mBookStatusRv.getParent(), false);
        mLikeBtn = (LikeButton) findViewById(R.id.art_like_btn);
        mCommentIb = (ImageButton) findViewById(R.id.art_comment_ib);
    }

    @Override
    protected void setView() {
        mCollapsingTb.setCollapsedTitleTextAppearance(R.style.TextAppearance_16);
        mCollapsingTb.setExpandedTitleTextAppearance(R.style.TextAppearance_12);

        setSupportActionBar(mToolbar);  //toolBar 的标准用法 作为 ActionBar显示，并启用返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mStatusAdapter = new BookStatusAdapter(R.layout.item_book_status, null, this);
        mBookStatusRv.setLayoutManager(new LinearLayoutManager(this));
        mBookStatusRv.setAdapter(mStatusAdapter);
        mBookStatusRv.setNestedScrollingEnabled(false);

        mDouAdapter = new DouCmtAdapter(R.layout.item_dou_comment, null);
        mDouCmtRv.setLayoutManager(new LinearLayoutManager(this));
        mDouCmtRv.setAdapter(mDouAdapter);
        mDouCmtRv.setNestedScrollingEnabled(false);
    }

    /**
     *  是否返回收集
     */
    @Override
    protected void setListener() {
        mLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                isBack = false;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                isBack = true;
            }
        });
    }

    @Override
    protected void getExtra() {
        mInfoUrl = getIntent().getStringExtra(ARGS_URL_BOOK);
        mPos = getIntent().getIntExtra(ARGS_POS_BOOK, -1);
    }

    @Override
    protected void initData() {
        mPresenter = new BookInfoPresenter(this);
        mPresenter.loadLibInfo(RetrofitClient.URL_BASE + mInfoUrl);

        //默认不返回
        isBack = false;
    }


    @Override
    protected void onStop() {
        super.onStop();

        boolean isLike = mLikeBtn.isLiked();

        //状态发生变化
        if (mBook.getLike() != isLike) {
            handleLike(isLike);
        }
    }

    /**
     * 处理 like
     */
    private void handleLike(boolean isLike) {
        if (isLike) {
            //已经持久化
            if (mBook.getId() != null) {
                if (!mBook.getLike()) {
                    mBook.setLike(true);
                    mBook.setUserId(PrefUtil.getUserId(this));
                    DBUtil.updateBook(mBook);
                }
            } else {
                //未持久化
                mBook.setIsbn(mIsbn);
                mBook.setCategory(mCategory);
                mBook.setName(mName);
                mBook.setImgUrl(mImgUrl);
                mBook.setInfoUrl(mInfoUrl);
                mBook.setLike(true);
                mBook.setUserId(PrefUtil.getUserId(this));
                DBUtil.insertBook(mBook);
            }
        } else {
            DBUtil.unlikeBook(mBook);
        }

        DBUtil.closeDB();
    }

    @Override
    public void setLibInfo(LibInfo libInfo) {
        mName = libInfo.getName();
        mIsbn = libInfo.getIsbn();
        mCategory = libInfo.getCategory();

        mCollapsingTb.setTitle(mName);
        mStatusAdapter.setNewData(libInfo.getStatusList());

        mPresenter.loadBook(mIsbn);
        //加载豆瓣信息
        mPresenter.loadDoubanInfo(ApiManager.BASE_DOU_BAN + mIsbn);
        //加载豆瓣评论
        mPresenter.loadDouBanCmt(ApiManager.BASE_DOU_BAN + mIsbn + "/reviews");
    }

    @Override
    public void setDouBanInfo(DouBanInfo douBanInfo) {

        if (douBanInfo != null) {
            mImgUrl = douBanInfo.getImages().getLarge();
            ViewUtil.useGlideBlur(this, mImgUrl, mBookBgIv);
        }

        ViewUtil.useGlideUrl(this, mImgUrl, mBookImageView);

    }

    @Override
    public void setDouBanCmt(List<DouComment> comments) {
        if (comments == null || comments.size() == 0) {
            mDouAdapter.setNewData(null);
            mDouAdapter.setEmptyView(mEmptyView);
        } else {
            mDouAdapter.setNewData(comments);
        }

        super.hideProgress();
    }

    @Override
    public void setComments(List<Comment> comments) {

    }

    @Override
    public void setBook(Book book) {
        mBook = book;
        if (mBook == null) {
            mBook = new Book();
        }

        //设置 likeButton
        mLikeBtn.setLiked(mBook.getLike());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(BuildConfig.DEBUG) Log.d(TAG, "onBackPressed: "+isBack);
        if (isBack) {
            Intent intent = new Intent();
            intent.putExtra(ARGS_POS_BOOK, mPos);
            setResult(RESULT_OK,intent);
        }
        finish();
    }
}
