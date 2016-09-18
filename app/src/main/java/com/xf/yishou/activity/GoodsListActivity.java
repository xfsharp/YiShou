package com.xf.yishou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.adapter.ListGoodsAdapter;
import com.xf.yishou.compare.CollectAscComparator;
import com.xf.yishou.compare.PriceAscComparator;
import com.xf.yishou.compare.PriceDescComparator;
import com.xf.yishou.compare.SaleDescComparator;
import com.xf.yishou.compare.TimeAscComprator;
import com.xf.yishou.entity.Goods;
import com.xf.yishou.http.XspHttp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GoodsListActivity extends FragmentActivity {
    private ListView lv_goods_list;
    private TextView tv_empty_goods;
    private RadioGroup rg_price;
    private TextView tv_sort_price;
    private TextView tv_sort_hot;
    private TextView tv_sort_new;
    private TextView tv_sort_collect;
    private RadioButton rb_price_asc;
    private RadioButton rb_price_desc;


    private List<Goods> model = new ArrayList<>();
    private ListGoodsAdapter adapter;

    private Comparator<Goods> comparator;

    private TextView[]  arrTv = new TextView[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        initView();
        initArr();


        lv_goods_list.setEmptyView(tv_empty_goods);

        final Intent intent = getIntent();
        String name = intent.getStringExtra("conditions");
        HashMap map = new HashMap();
        map.put("type" , "1");
        map.put("conditions" , name);

        XspHttp http = XspHttp.newXspHttp();
        http.getHttpData(UtilsURLPath.getGoodsPath, "POST", map, new XspHttp.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                if (result != null){
                    Goods[] arr = new Gson().fromJson(result , Goods[].class);
                    model.addAll(Arrays.asList(arr));
                    comparator = new PriceAscComparator();
                    Collections.sort(model , comparator);
                    rg_price.check(R.id.rb_price_asc);
                    tv_sort_price.setTextColor(Color.RED);
                    if (adapter == null){
                        adapter = new ListGoodsAdapter(model , getLayoutInflater());
                        lv_goods_list.setAdapter(adapter);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 初始化数组
     * */
    private void initArr() {
        arrTv[0] = tv_sort_price;
        arrTv[1] = tv_sort_hot;
        arrTv[2] = tv_sort_new;
        arrTv[3] = tv_sort_collect;
    }

    /**
     * 监听其他的排序
     * */
    private int previous = 0;
    private boolean isAse = true;
    public void sortClick(View v){
        arrTv[previous].setTextColor(Color.BLACK);
        int id = v.getId();
        switch (id){
            case R.id.ll_price:
                isAse = !isAse;
                previous = 0;
                break;
            case R.id.ll_hot:
                comparator = new SaleDescComparator();
                previous = 1;
                break;
            case R.id.ll_new:
                comparator = new TimeAscComprator();
                previous = 2;
                break;
            case R.id.ll_collect:
                comparator = new CollectAscComparator();
                previous = 3;
                break;
        }

        if (previous == 0){
            if (isAse){
                comparator = new PriceAscComparator();
                rg_price.check(R.id.rb_price_asc);
            }else {
                comparator = new PriceDescComparator();
                rg_price.check(R.id.rb_price_desc);
            }
        }

        if (previous != 0){
            rb_price_asc.setChecked(false);
            rb_price_desc.setChecked(false);
        }
        arrTv[previous].setTextColor(Color.RED);

        Collections.sort(model , comparator);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        lv_goods_list = (ListView) findViewById(R.id.lv_goods_list);
        tv_empty_goods = (TextView) findViewById(R.id.tv_empty_goods);
        rg_price = (RadioGroup) findViewById(R.id.rg_price);
        tv_sort_price = (TextView) findViewById(R.id.tv_sort_price);
        tv_sort_hot = (TextView) findViewById(R.id.tv_sort_hot);
        tv_sort_new = (TextView) findViewById(R.id.tv_sort_new);
        tv_sort_collect = (TextView) findViewById(R.id.tv_sort_collect);
        rb_price_asc = (RadioButton) findViewById(R.id.rb_price_asc);
        rb_price_desc = (RadioButton) findViewById(R.id.rb_price_desc);
    }
}
