package com.xf.yishou.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xf.yishou.R;
import com.xf.yishou.adapter.Adapter_Home_Hot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bwfadmin on 2016/9/1.
 */
public class Fragment_Home extends Fragment{

    private View view;

    private List<Fragment> fragmentList;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * @param int resource 			布局文件id
         * @param ViewGroup root		视图组对象，其实就是给当前Fragment指定容器
         * @param boolean attachToRoot	是否附着到根上，因为已经有容器了，所以不需要附着到根上, 传false，否则异常
         * 			java.lang.IllegalStateException: The specified child already has a parent.
         * 			You must call removeView() on the child's parent first.
         * */
        view = inflater.inflate(R.layout.fragment_home,container,false);

        fragmentList = new ArrayList<Fragment>();

        fragmentList.add(new Fragment_Hot_01());
        fragmentList.add(new Fragment_Hot_02());
        fragmentList.add(new Fragment_Hot_03());

        Adapter_Home_Hot hotAdapter = new Adapter_Home_Hot(getFragmentManager(),fragmentList);

        viewPager = (ViewPager)view.findViewById(R.id.vp_hot);
        viewPager.setAdapter(hotAdapter);



        return view;
    }
}
