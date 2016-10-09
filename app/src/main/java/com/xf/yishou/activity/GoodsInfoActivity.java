package com.xf.yishou.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.adapter.ViewPagerAdapter;
import com.xf.yishou.entity.Goods;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xsp on 2016/9/29.
 */

public class GoodsInfoActivity extends FragmentActivity{
    private TextView tv_goodstitle_info_name;
    private TextView tv_goods_info_name;
    private TextView tv_goods_nowmoney;
    private TextView tv_goods_info_oldmoney;
    private TextView tv_goods_publish_time;
    private TextView tv_goods_collocetmun;
    private TextView tv_goods_details_info;
    private TextView tv_goods_publish_people;
    private TextView tv_goods_info_phone;
    private ViewPager vp_goods_info_image;

    private Goods goods;
    private List<String> imagePath;
    private List<View> viewPagerModel = new ArrayList<View>();
    private ViewPagerAdapter adapter;

    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_goodsinfo);
        initView();
        addModel();
        setAdapter();
        showData();
    }

    /**
     * 展示获取的数据
     * */
    private void showData() {
        intent = getIntent();
        goods = (Goods)intent.getSerializableExtra("goods");
        tv_goodstitle_info_name.setText(goods.getGoodsName());
        tv_goods_info_name.setText(goods.getGoodsName());
        tv_goods_nowmoney.setText("¥" + goods.getPrice() + "");
        tv_goods_info_oldmoney.setText("¥" + goods.getOriginalprice() + "");
        tv_goods_info_oldmoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_goods_publish_time.setText(goods.getTime());
        tv_goods_collocetmun.setText("共有" +  (int)(Math.random()*1000) + "人收藏了此商品");
        tv_goods_details_info.setText(goods.getGoodsinfo());
        tv_goods_publish_people.setText(goods.getgName());
        tv_goods_info_phone.setText(goods.getPhone());
        vp_goods_info_image.setAdapter(adapter);
    }

    /**
     * 设置适配器
     * */
    private void setAdapter() {
        adapter = new ViewPagerAdapter(viewPagerModel);
    }

    /**
     * 添加view视图
     * */
    private void addModel() {
        LayoutInflater inflater = getLayoutInflater();
        viewPagerModel.add(inflater.inflate(R.layout.item_vp_goodsinfo, null));
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        tv_goodstitle_info_name = (TextView) findViewById(R.id.tv_goodstitle_info_name);
        tv_goods_info_name = (TextView) findViewById(R.id.tv_goods_info_name);
        tv_goods_nowmoney = (TextView) findViewById(R.id.tv_goods_nowmoney);
        tv_goods_info_oldmoney = (TextView) findViewById(R.id.tv_goods_info_oldmoney);
        tv_goods_publish_time = (TextView) findViewById(R.id.tv_goods_publish_time);
        tv_goods_collocetmun = (TextView) findViewById(R.id.tv_goods_collocetmun);
        tv_goods_details_info = (TextView) findViewById(R.id.tv_goods_details_info);
        tv_goods_publish_people = (TextView) findViewById(R.id.tv_goods_publish_people);
        tv_goods_info_phone = (TextView) findViewById(R.id.tv_goods_info_phone);
        vp_goods_info_image = (ViewPager) findViewById(R.id.vp_goods_info_image);

    }

    /**
     * 打电话和发短息的监听
     * */
    public void rb_phoneandmsg(View v){
        switch (v.getId()){
            case R.id.rb_goods_info_phone:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + goods.getPhone()));
                startActivity(intent);
                break;
            case R.id.rb_goods_info_msg:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + goods.getPhone()));
                intent.putExtra("sms_body" , "您好！我是在易手网看见您要出售这件商品！请问能具体交流一下吗？");
                startActivity(intent);
                break;
        }
    }
}
