package com.xf.yishou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.entity.Category;

import java.util.List;

/**
 * Created by xsp on 2016/9/12.
 */
public class GridChildAdapter<Secondary> extends SuperAdapter{
    private List<Category.Secondary> model;
    private LayoutInflater inflater;

    public GridChildAdapter(Category category, LayoutInflater inflater) {
        super(category.getSortval(), inflater);
        this.model = category.getSortval();
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null){
            hodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.item_grid_child , null);
            hodler.tv = (TextView) convertView;
            convertView.setTag(hodler);
        }else{
            hodler = (ViewHodler) convertView.getTag();
        }
        Category.Secondary secondary = model.get(position);
        hodler.tv.setText(secondary.getSortname());
        return convertView;
    }

    class ViewHodler{
        TextView tv;
    }
}
