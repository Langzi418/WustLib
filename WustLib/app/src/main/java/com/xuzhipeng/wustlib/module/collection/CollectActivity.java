package com.xuzhipeng.wustlib.module.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xuzhipeng.wustlib.BuildConfig;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.base.BaseActivity;
import com.xuzhipeng.wustlib.common.util.PrefUtil;
import com.xuzhipeng.wustlib.db.Book;
import com.xuzhipeng.wustlib.db.DBUtil;
import com.xuzhipeng.wustlib.module.adapter.CollectAdapter;
import com.xuzhipeng.wustlib.module.info.BookInfoActivity;
import com.xuzhipeng.wustlib.presenter.CollectPresenter;
import com.xuzhipeng.wustlib.view.ICollectView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CollectActivity extends BaseActivity implements ICollectView {

    private static final String TAG = "CollectActivity";

    private SwipeMenuRecyclerView mCollectRv;
    private CollectAdapter mCollectAdapter;
    private List<Book> mBooks;
    private CollectPresenter mPresenter;
    private View mEmptyView;

    public static Intent newIntent(Context context) {
        return new Intent(context, CollectActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        mCollectRv = (SwipeMenuRecyclerView) findViewById(R.id.collect_rv);

        mEmptyView = getLayoutInflater().inflate(R.layout.view_empty,
                (ViewGroup) mCollectRv.getParent(), false);
    }


    @Override
    protected void setView() {
        setToolbar(R.string.collection);

        mCollectAdapter = new CollectAdapter(R.layout.item_collection, null, this);
        mCollectRv.setAdapter(mCollectAdapter);
        mCollectRv.setLayoutManager(new LinearLayoutManager(this));
        //滑动删除

        mCollectRv.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int
                    viewType) {
                if (mBooks != null && mBooks.size() > 0) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(CollectActivity.this);
                    deleteItem
                            .setText(R.string.delete)
                            .setImage(R.drawable.ic_delete)
                            .setBackgroundColorResource(R.color.red_light)
                            .setWidth(180)
                            .setHeight(MATCH_PARENT);
                    swipeRightMenu.addMenuItem(deleteItem);
                }
            }
        });

    }

    @Override
    protected void setListener() {
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Book book = mBooks.get(position);
                Intent intent = new Intent(CollectActivity.this, BookInfoActivity.class);
                intent.putExtra(BookInfoActivity.ARGS_URL_BOOK, book.getInfoUrl());
                intent.putExtra(BookInfoActivity.ARGS_POS_BOOK, position);
                startActivityForResult(intent, 1);
            }
        });

        //滑动删除监听
        mCollectRv.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                int position = menuBridge.getAdapterPosition();
                if (mBooks.size() > position) {
                    //数据库处理
                    DBUtil.unlikeBook(mBooks.get(position));
                    DBUtil.closeDB();
                    // 删除数据，并更新adapter。
                    mBooks.remove(position);
                    mCollectAdapter.notifyItemRemoved(position);
                    //是否是空布局
                    if (mBooks.size() == 0) {
                        mCollectAdapter.setEmptyView(mEmptyView);
                    }
                }

            }
        });
    }

    @Override
    protected void initData() {
        mPresenter = new CollectPresenter(this);

        long userId = PrefUtil.getUserId(this);
        if (userId != 0L) {
            mPresenter.loadBooks(userId);
        }
    }


    @Override
    public void setBooks(List<Book> books) {

        if (books == null || books.size() == 0) {
            mCollectAdapter.setNewData(null);
            mCollectAdapter.setEmptyView(mEmptyView);

        } else {
            mBooks = books;
            mCollectAdapter.setNewData(mBooks);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(BookInfoActivity.ARGS_POS_BOOK, -1);
                    if (position != -1) {
                        deleteCollectItem(position);
                    }
                }
                break;
        }
    }


    /**
     * 删除收藏项
     */
    private void deleteCollectItem(int pos) {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "deleteCollectItem: " + pos);
        mBooks.remove(pos);
        mCollectAdapter.notifyItemRemoved(pos);
        if (mBooks.size() == 0) {
            mCollectAdapter.setEmptyView(mEmptyView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
