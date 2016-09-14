package com.xf.yishou.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.entity.Goods;
import com.xf.yishou.http.ImageLoader;

import java.util.List;

/**
 * Created by xsp on 2016/9/14.
 */
public class GridHotAdapter extends SuperAdapter<Goods>{
    private List<Goods> model;
    private LayoutInflater inflater;

    public GridHotAdapter(List<Goods> model, LayoutInflater inflater) {
        super(model, inflater);
        this.model = model;
        this.inflater = inflater;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridHodler hodler = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_grid_hot , null);
            hodler = new GridHodler();
            hodler.iv_hot_img = (ImageView) convertView.findViewById(R.id.iv_hot_img);
            hodler.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            convertView.setTag(hodler);
        }else{
            hodler = (GridHodler) convertView.getTag();
        }

        //设置商品名称
        Goods goods = model.get(position);
        String goodsName = goods.getGoodsName();
        hodler.tv_goods_name.setText(goodsName);

        //设置图片
        List<String> imgList = goods.getImagePath();
        if (imgList != null && imgList.size() > 0){
            String imgName = imgList.get(0);
            ImageLoader loader = ImageLoader.newInstance();
            loader.LoadImage(imgName , hodler.iv_hot_img);
        }

        return convertView;
    }

    static class GridHodler{
        ImageView iv_hot_img;
        TextView tv_goods_name;
    }
}
