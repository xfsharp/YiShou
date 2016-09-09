package com.xf.yishou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xsp on 2016/9/6.
 */
public class Adapter_Home_Hot extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    public Adapter_Home_Hot(FragmentManager fm , List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.isEmpty() ? 0 :  mFragments.size();
    }
}
