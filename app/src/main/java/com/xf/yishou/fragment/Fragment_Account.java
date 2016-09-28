package com.xf.yishou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.adapter.ListGoodsAdapter;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.entity.Goods;
import com.xf.yishou.http.XspHttp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by xsp on 2016/9/1.
 */
public class Fragment_Account extends Fragment {
    private  RadioGroup mRgSelf;
    private ListView mLvSelf;
    private SwipeRefreshLayout mRefreshSelf;
    private LinearLayout ll_acc_notlogin;
    private View view;

    private ListGoodsAdapter adapter;
    private List<Goods> model = new ArrayList<>();

    private List<Goods> collectList = new ArrayList<>();
    private List<Goods> publishList = new ArrayList<>();

    private LayoutInflater inflater;
    private Gson gson = new Gson();

    private boolean isFirst = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        this.inflater = inflater;
        initData();
        setListener();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 点击事件的监听和更新的监听
     * */
    private void setListener() {
        mRgSelf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                model.clear();
                if (checkedId == R.id.rb_collected){
                    model.addAll(collectList);
                }else if (checkedId == R.id.rb_published){
                    model.addAll(publishList);
                }
                adapter.notifyDataSetChanged();
            }
        });

        mRefreshSelf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mRgSelf.check(R.id.rb_collected);
            }
        });
    }

    private void initData() {
        if(MarketApp.user != null){
            XspHttp xspHttp = XspHttp.newXspHttp();
            HashMap collectMap = new HashMap();
            HashMap publishMap = new HashMap();
            //请求商品的列表
            String userName = MarketApp.user.getUserName();
            collectMap.put("userName" , userName);
            publishMap.put("type" , "2");
            publishMap.put("conditions" , userName);
            String collectUrl = UtilsURLPath.queryCollectList;
            String publishUrl = UtilsURLPath.getGoodsPath;

            String method = "POST";

            xspHttp.getHttpData(collectUrl, method, collectMap, new XspHttp.OnCompleteListener() {
                @Override
                public void onComplete(String result) {
                    showData(result , R.id.rb_collected);
                    isFirst = false;
                    if (!isFirst){
                        mRefreshSelf.setRefreshing(isFirst);
                    }
                }
            });

            xspHttp.getHttpData(publishUrl, method, publishMap, new XspHttp.OnCompleteListener() {
                @Override
                public void onComplete(String result) {
                    showData(result , R.id.rb_published);
                    isFirst = false;
                }
            });
            ll_acc_notlogin.setVisibility(View.GONE);
            mRgSelf.setVisibility(View.VISIBLE);
        }else {
            ll_acc_notlogin.setVisibility(View.VISIBLE);
            mRgSelf.setVisibility(View.GONE);
        }
    }


    /**
     * 展示数据的方法
     * */
    private void showData(String result, int id) {
        if (result != null){
            model.clear();
            Goods[] arr = gson.fromJson(result, Goods[].class);
            List<Goods> tmpList = Arrays.asList(arr);
            if (id == R.id.rb_collected){
                collectList.clear();
                collectList.addAll(tmpList);
                model.addAll(collectList);
                if (adapter == null){
                    adapter = new ListGoodsAdapter(model , inflater);
                    mLvSelf.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }
            }else if (id == R.id.rb_published){
                publishList.clear();
                publishList.addAll(tmpList);
               // model.addAll(publishList);
            }

        }
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        mRgSelf = (RadioGroup) view.findViewById(R.id.rg_self);
        mLvSelf = (ListView) view.findViewById(R.id.lv_self);
        mRefreshSelf = (SwipeRefreshLayout) view.findViewById(R.id.refresh_self);
        ll_acc_notlogin = (LinearLayout) view.findViewById(R.id.ll_acc_notlogin);
    }
}