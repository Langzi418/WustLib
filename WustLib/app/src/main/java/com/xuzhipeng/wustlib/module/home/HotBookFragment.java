package com.xuzhipeng.wustlib.module.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuzhipeng.wustlib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotBookFragment extends Fragment {


    public HotBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_book, container, false);
    }

}
