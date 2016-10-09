package com.xf.yishou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xsp on 2016/10/8.
 */

public class ViewPagerAdapter extends PagerAdapter{
    private List<View> model;

    public ViewPagerAdapter(List<View> model) {
        this.model = model;
    }

    @Override
    public int getCount() {
        return (model == null) ? 0 : model.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = model.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = model.get(position);
        container.removeView(view);
    }
}
