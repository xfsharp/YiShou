package com.xf.yishou.adapter;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xsp on 2016/9/12.
 */
public abstract class SuperAdapter extends BaseAdapter{
    private List model;
    private LayoutInflater inflater;

    public SuperAdapter(List model, LayoutInflater inflater) {
        this.model = model;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return model == null ? 0 : model.size();
    }

    @Override
    public Object getItem(int position) {
        return model == null ? null : model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
