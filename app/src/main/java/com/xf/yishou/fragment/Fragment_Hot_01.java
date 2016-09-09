package com.xf.yishou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xf.yishou.R;
import com.xf.yishou.http.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xsp on 2016/9/7.
 */
public class Fragment_Hot_01 extends Fragment{
    private View view;
    private ImageView iv_home_hot;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hot, container,false);

        iv_home_hot = (ImageView) view.findViewById(R.id.iv_home_hot);

        ImageLoader.newInstance().LoadImage("1460975692427.jpg",iv_home_hot);


        return view;

    }
}
