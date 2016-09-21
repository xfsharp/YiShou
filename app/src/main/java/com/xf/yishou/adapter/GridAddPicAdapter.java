package com.xf.yishou.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xf.yishou.R;

import java.util.List;

/**
 * Created by xsharp on 2016/9/21.
 */

public class GridAddPicAdapter extends SuperAdapter<Bitmap>{
    private List<Bitmap> model;
    private LayoutInflater inflater;

    public GridAddPicAdapter(List<Bitmap> model, LayoutInflater inflater) {
        super(model, inflater);
        this.model = model;
        this.inflater = inflater;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddPicHodler addPicHodler = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_grid_addpic , null);
            addPicHodler = new AddPicHodler();
            addPicHodler.iv_addpic = (ImageView) convertView.findViewById(R.id.iv_addpic);
            convertView.setTag(addPicHodler);
        }else{
            addPicHodler = (AddPicHodler) convertView.getTag();
        }

        Bitmap bitmap = model.get(position);
        addPicHodler.iv_addpic.setImageBitmap(bitmap);

        return convertView;
    }


    static class AddPicHodler{
        ImageView iv_addpic;
    }
}
