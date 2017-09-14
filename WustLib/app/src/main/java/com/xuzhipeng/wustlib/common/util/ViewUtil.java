package com.xuzhipeng.wustlib.common.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.tablayout.SlidingTabLayout;
import com.xuzhipeng.wustlib.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/8/18
 */

public class ViewUtil {

    /**
     * 默认数据加载进度框
     */
    public static MaterialDialog getProgressBar(Context context, int contentId){
        return new MaterialDialog.Builder(context)
                .content(contentId)
                .progress(true,0)
                .show();
    }


    /**
     * 活动中设置viewpager
     */
    public static void setViewPager(PagerAdapter adapter, Activity activity){
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedPosition / 2 + 0.5f);
                page.setScaleY(normalizedPosition / 2 + 0.5f);
            }
        });

        SlidingTabLayout viewPagerTab = (SlidingTabLayout)
                activity.findViewById(R.id.view_pager_tab);
        viewPagerTab.setViewPager(viewPager);
    }



    /**
     *  glide加载url图片
     */
    public static void useGlideUrl(Context context,String url,ImageView view){
        Glide.with(context)
                .load(url)
                .error(R.drawable.default_book)
                .into(view);
    }

    /**
     *  glide 图片模糊
     */
    public static void useGlideBlur(Context context,String url,ImageView iv){
        Glide.with(context).load(url)
                .bitmapTransform(new BlurTransformation(context, 20),
                        new CenterCrop(context))
                .into(iv);

    }

}
