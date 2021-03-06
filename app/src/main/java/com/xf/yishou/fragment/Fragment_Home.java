package com.xf.yishou.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.activity.GoodsInfoActivity;
import com.xf.yishou.adapter.GridHotAdapter;
import com.xf.yishou.adapter.HomeHotAdapter;
import com.xf.yishou.adapter.ListGoodsAdapter;
import com.xf.yishou.entity.Goods;
import com.xf.yishou.http.XspHttp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by xsp on 2016/9/1.
 */
public class Fragment_Home extends Fragment{
    private View view;

    private List<Fragment> fragmentList;
    private ViewPager viewPager;

    private List<Goods> pagerModel;
    private List<Goods> gridModel;
    private List<Goods> listModel;

    private ListGoodsAdapter listAdapter;
    private GridHotAdapter gridAdapter;
    private HomeHotAdapter hotAdapter;
    private LayoutInflater inflater;

    private GridView gv_hot_goods;
    private ListView lv_all_goods;

    int position = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * @param int resource 			布局文件id
         * @param ViewGroup root		视图组对象，其实就是给当前Fragment指定容器
         * @param boolean attachToRoot	是否附着到根上，因为已经有容器了，所以不需要附着到根上, 传false，否则异常
         * 			java.lang.IllegalStateException: The specified child already has a parent.
         * 			You must call removeView() on the child's parent first.
         * */
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = (ViewPager)view.findViewById(R.id.vp_hot);
        lv_all_goods = (ListView) view.findViewById(R.id.lv_all_goods);
        gv_hot_goods = (GridView) view.findViewById(R.id.gv_hot_goods);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new Fragment_Hot_01());
        fragmentList.add(new Fragment_Hot_02());
        fragmentList.add(new Fragment_Hot_03());
        hotAdapter = new HomeHotAdapter(getFragmentManager(),fragmentList);
        viewPager.setAdapter(hotAdapter);


        initData();
        setListener();
        setAutoViewPager();
        return view;
    }

    @Override
    public void onDestroy() {
        th.interrupt();
        super.onDestroy();
    }


    /**
     * 轮播效果
     * */
    private void setAutoViewPager() {
        th.start();
    }

    Thread th = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(2000);
                    if (position >= fragmentList.size()-1){
                        position = -1;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(++position);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    });


    /**
     * 各种监听事件
     * */
    private void setListener() {
        lv_all_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = listModel.get(position);
                Intent intent = new Intent(getActivity() , GoodsInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods" , goods);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gv_hot_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = gridModel.get(position);
                Intent intent = new Intent(getActivity() , GoodsInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods" , goods);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    /**
     * 加载总数据
     * */
    private void initData() {


        XspHttp http = XspHttp.newXspHttp();
        HashMap<String , String > map = new HashMap();
        map.put("type" , "0");
        map.put("conditions" , "1");
        http.getHttpData(UtilsURLPath.getGoodsPath, "GET", map, new XspHttp.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                if (result != null) {
                    Gson gson = new Gson();
                    Goods[] arr = gson.fromJson(result, Goods[].class);
                    List total = Arrays.asList(arr);
                    //pagerModel = total.subList(0 , 5);
                    gridModel = total.subList(5 , 15);
                    listModel = total.subList(15 , total.size());
                    showData();
                }
            }
        });
    }

    /**
     *将获取到到数据展示到界面上
     * */
    private void showData() {

        //GridView的数据展示
        if (gridAdapter == null){
            gridAdapter = new GridHotAdapter(gridModel , inflater);
            gv_hot_goods.setAdapter(gridAdapter);
        }else {
            gridAdapter.notifyDataSetChanged();
        }

        //ListView的数据展示
        if (listAdapter == null ){
            listAdapter = new ListGoodsAdapter(listModel , inflater);
            lv_all_goods.setAdapter(listAdapter);
        }else {
            listAdapter.notifyDataSetChanged();
        }
    }
}