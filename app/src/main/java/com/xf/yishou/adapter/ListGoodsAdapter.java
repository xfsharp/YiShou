package com.xf.yishou.adapter;

import android.graphics.Paint;
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
 * Created by xsp on 2016/9/13.
 */
public class ListGoodsAdapter extends SuperAdapter{
    private List<Goods> model;
    private LayoutInflater inflater;
    private ImageLoader loader;

    public ListGoodsAdapter(List model, LayoutInflater inflater) {
        super(model, inflater);
        this.model = model;
        this.inflater = inflater;
        loader = ImageLoader.newInstance();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_goods , null);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_goods_title);
            holder.tv_now = (TextView) convertView.findViewById(R.id.tv_now_price);
            holder.tv_old = (TextView) convertView.findViewById(R.id.tv_old_price);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_pub_time);

            convertView.setTag(holder);
        }else {
            convertView = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Goods goods = model.get(position);
        holder.tv_title.setText(goods.getGoodsName());
        holder.tv_now.setText("¥" + goods.getPrice() + "");
        holder.tv_old.setText("¥" + goods.getOriginalprice() + "");
        //中间的划线
        holder.tv_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        // TextView的底部划横线
        //holder.tv_old.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        holder.tv_time.setText(goods.getTime());

        List<String> list = goods.getImagePath();
        if (list != null && list.size() > 0){
            String index = list.get(0);
            loader.LoadImage(index , holder.iv_icon);
        }
        return convertView;
    }

   public static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_old;
        TextView tv_now;
        TextView tv_time;
    }
}
