package com.xuzhipeng.wustlib.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.xuzhipeng.wustlib.module.adapter.CommentAdapter;
import com.xuzhipeng.wustlib.module.adapter.DouCmtAdapter;
import com.xuzhipeng.wustlib.module.mylib.LoginActivity;
import com.xuzhipeng.wustlib.net.RetrofitClient;
import com.xuzhipeng.wustlib.net.api.ApiManager;
import com.xuzhipeng.wustlib.presenter.BookInfoPresenter;
import com.xuzhipeng.wustlib.view.IBookInfoView;

import java.util.Date;
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
    private CommentAdapter mCommentAdapter;

    private BookStatusAdapter mStatusAdapter;
    private BookInfoPresenter mPresenter;

    private DouCmtAdapter mDouAdapter;
    private Toolbar mToolbar;
    private RecyclerView mDouCmtRv;
    private View mEmptyView;
    private View mEmptyView2;
    private View mEmptyView3;
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
        mEmptyView2 = getLayoutInflater().inflate(
                R.layout.view_empty, (ViewGroup) mDouCmtRv.getParent(), false);
        mEmptyView3 = getLayoutInflater().inflate(
                R.layout.view_empty, (ViewGroup) mBookCommentRv.getParent(), false);


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

        mCommentAdapter = new CommentAdapter(R.layout.item_comment, null);
        mBookCommentRv.setLayoutManager(new LinearLayoutManager(this));
        mBookCommentRv.setAdapter(mCommentAdapter);
        mBookCommentRv.setNestedScrollingEnabled(false);

        mDouAdapter = new DouCmtAdapter(R.layout.item_dou_comment, null);
        mDouCmtRv.setLayoutManager(new LinearLayoutManager(this));
        mDouCmtRv.setAdapter(mDouAdapter);
        mDouCmtRv.setNestedScrollingEnabled(false);
    }

    /**
     * 是否返回收集
     */
    @Override
    protected void setListener() {
        //是否返回
        mLikeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                checkLogin();
                isBack = false;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                isBack = true;
            }
        });

        mCommentIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                createComment();
            }
        });
    }

    /**
     *  检验登录
     */
    private void checkLogin(){
        if(! PrefUtil.getSuccess(BookInfoActivity.this)){
            startActivity(LoginActivity.newIntent(BookInfoActivity.this));
            Toast.makeText(BookInfoActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建评论
     */
    private void createComment() {
        new MaterialDialog.Builder(this)
                .title(R.string.create_comment)
                .cancelable(false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .customView(R.layout.view_comment_create, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        handleCommentDB(dialog);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 处理评论
     */
    private void handleCommentDB(MaterialDialog dialog) {
        View view = dialog.getCustomView();
        if (view == null) {
            return;
        }
        EditText editText = (EditText) view.findViewById(R.id.comment_et);
        String content = editText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(BookInfoActivity.this, R.string.no_content, Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDate(new Date());
        comment.setUsername(PrefUtil.getUserName(this));

        if (mBook.getId() == null) {
            //未持久化，则持久化
            mBook.setIsbn(mIsbn);
            mBook.setCategory(mCategory);
            mBook.setName(mName);
            mBook.setImgUrl(mImgUrl);
            mBook.setInfoUrl(mInfoUrl);
            mBook.setLike(false);
            mBook.setUserId(-1L);
            DBUtil.insertBook(mBook);
        }
        comment.setBookId(mBook.getId());

        mCommentAdapter.insertToFirst(comment);
        DBUtil.insertComment(comment);
        DBUtil.closeDB();
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

        //状态发生变化,并且处于登录状态
        if (PrefUtil.getSuccess(this) && mBook.getLike() != isLike) {
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

        if (libInfo.getStatusList() == null || libInfo.getStatusList().size() == 0) {
            mStatusAdapter.setNewData(null);
            mStatusAdapter.setEmptyView(mEmptyView);
        } else {
            mStatusAdapter.setNewData(libInfo.getStatusList());
        }

        mPresenter.loadBook(mIsbn);
        //加载豆瓣信息
        mPresenter.loadDoubanInfo(ApiManager.BASE_DOU_BAN + mIsbn);

        //加载校内评论
        mPresenter.loadComment(mIsbn);

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
            mDouAdapter.setEmptyView(mEmptyView2);
        } else {
            mDouAdapter.setNewData(comments);
        }

        super.hideProgress();
    }

    @Override
    public void setComments(List<Comment> comments) {
        if (comments == null || comments.size() == 0) {
            mCommentAdapter.setNewData(null);
            mCommentAdapter.setEmptyView(mEmptyView3);
        } else {
            mCommentAdapter.setNewData(comments);
        }
    }

    @Override
    public void setBook(Book book) {
        if (book == null) {
            mBook = new Book();
        }else {
            mBook = book;
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

        if (BuildConfig.DEBUG)
            Log.d(TAG, "onBackPressed: " + isBack);
        if (isBack) {
            Intent intent = new Intent();
            intent.putExtra(ARGS_POS_BOOK, mPos);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
