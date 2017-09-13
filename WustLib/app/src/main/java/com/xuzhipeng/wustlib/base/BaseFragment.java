package com.xuzhipeng.wustlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xuzhipeng.wustlib.R;
import com.xuzhipeng.wustlib.common.util.ViewUtil;
import com.xuzhipeng.wustlib.view.ILoadView;


/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/19
 */

public abstract class BaseFragment extends Fragment implements ILoadView{

    private static final String TAG = "BaseFragment";

    private MaterialDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);

        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    protected void initData(){}

    protected abstract void initView(View view);

    protected abstract int getLayoutId();


    @Override
    public void showProgress() {
        dialog  = ViewUtil.getProgressBar(getActivity(), R.string.load_data);
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }


}
