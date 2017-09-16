package com.xuzhipeng.wustlib.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.view.ILoadView;


/**
 * Author: xuzhipeng
 * Email: langzi_xzp@foxmail.com
 * Date: 2017/7/7
 */

public abstract class BaseActivity extends AppCompatActivity
        implements ILoadView{

//    protected BGASwipeBackHelper mSwipeBackHelper;


    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        setStatusBar();
        setView();
        setListener();
        getExtra();
        initData();

    }

    /**
     *  设置状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                ContextCompat.getColor(this,R.color.colorPrimary));
    }

    /**
     * 初始设置布局
     */
    protected void setView() {

    }


    /*
     * 加载数据
     */
    protected void initData() {

    }

    /*
     * 获取intent 数据
     */
    protected void getExtra() {

    }

    /*
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * toolbar
     * @param titleId  标题id
     */
    protected void setToolbar(int  titleId, Drawable draw){
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.base_toolbar_title);

        toolbarTitle.setText(getString(titleId));
        if(draw!=null){
            toolbar.setBackground(draw);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }



    /**
     * 工具栏菜单项选择
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProgress() {
        dialog  = ViewUtil.getProgressBar(this, R.string.load_data);
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }

//    /**
//     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
//     */
//    private void initSwipeBackFinish() {
//        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
//
//        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
//        // 下面几项可以不配置，这里只是为了讲述接口用法。
//
//        // 设置滑动返回是否可用。默认值为 true
//        mSwipeBackHelper.setSwipeBackEnable(true);
//        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
//        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
//        // 设置是否是微信滑动返回样式。默认值为 true
//        mSwipeBackHelper.setIsWeChatStyle(true);
//        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
//        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
//        // 设置是否显示滑动返回的阴影效果。默认值为 true
//        mSwipeBackHelper.setIsNeedShowShadow(true);
//        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
//        mSwipeBackHelper.setIsShadowAlphaGradient(true);
//        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
//        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
//    }
//
//    /**
//     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
//     *
//     * @return
//     */
//    @Override
//    public boolean isSupportSwipeBack() {
//        return true;
//    }
//
//    /**
//     * 正在滑动返回
//     *
//     * @param slideOffset 从 0 到 1
//     */
//    @Override
//    public void onSwipeBackLayoutSlide(float slideOffset) {
//    }
//
//    /**
//     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
//     */
//    @Override
//    public void onSwipeBackLayoutCancel() {
//    }
//
//    /**
//     * 滑动返回执行完毕，销毁当前 Activity
//     */
//    @Override
//    public void onSwipeBackLayoutExecuted() {
//        mSwipeBackHelper.swipeBackward();
//    }
//
//    @Override
//    public void onBackPressed() {
//        // 正在滑动返回的时候取消返回按钮事件
//        if (mSwipeBackHelper.isSliding()) {
//            return;
//        }
//        mSwipeBackHelper.backward();
//    }
//
//    /**
//     * 设置状态栏颜色
//     *
//     * @param color
//     */
//    protected void setStatusBarColor(@ColorInt int color) {
//        StatusBarUtil.setColor(this,color);
//    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
