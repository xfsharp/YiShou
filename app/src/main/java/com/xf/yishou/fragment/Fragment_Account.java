package com.xf.yishou.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xf.yishou.R;

/**
 * Created by bwfadmin on 2016/9/1.
 */
public class Fragment_Account extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account,container,false);
        return view;
    }
}
