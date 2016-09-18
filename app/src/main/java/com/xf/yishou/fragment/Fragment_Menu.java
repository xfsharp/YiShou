package com.xf.yishou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.activity.LoginActivity;
import com.xf.yishou.activity.MainActivity;
import com.xf.yishou.activity.RLoginActivity;
import com.xf.yishou.application.MarketApp;

/**
 * Created by xsp on 2016/9/1.
 */
public class Fragment_Menu extends Fragment implements View.OnClickListener{
    private MainActivity main;

    private View view;
    private int clickMenuIndex = 0;
    private FragmentTransaction transaction;
    private Fragment[] frag_main_arr;

    private RadioButton rb_home;
    private RadioButton rb_account;
    private RadioButton rb_cart;
    private RadioButton rb_search;
    private LinearLayout ll_user;

    private String[] arr_title;

    public TextView tv_user_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_meun,container,false);

        arr_title = getResources().getStringArray(R.array.arr_title);

        initView();

        setOnClickListener();

        main = (MainActivity)getActivity();

        frag_main_arr = main.getFragArr();

        initMenu();

        return view;
    }
    /**
     *初始化菜单栏
     * */
    private void initMenu() {

        rb_home.setChecked(true);
        main.getTitleTv().setText(arr_title[0]);

    }

    private void setOnClickListener() {
        rb_home.setOnClickListener(this);
        rb_account.setOnClickListener(this);
        rb_cart.setOnClickListener(this);
        rb_search.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void initView() {
        rb_home = (RadioButton) view.findViewById(R.id.rb_home);
        rb_account = (RadioButton) view.findViewById(R.id.rb_account);
        rb_cart = (RadioButton) view.findViewById(R.id.rb_cart);
        rb_search = (RadioButton) view.findViewById(R.id.rb_search);
        ll_user = (LinearLayout)view.findViewById(R.id.ll_user);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
    }


    /**
     * 监听菜单选项的点击事件
     * */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rb_home:
                clickMenuIndex = 0;
                break;
            case R.id.rb_account:
                clickMenuIndex = 1;
                break;
            case R.id.rb_cart:
                clickMenuIndex = 2;
                break;
            case R.id.rb_search:
                clickMenuIndex = 3;
                break;
            case R.id.ll_user:
                Intent intent = new Intent();
                //intent.setClass(getContext(), LoginActivity.class);
                intent.setClass(getContext(), RLoginActivity.class);
                startActivityForResult(intent , 1);
        }

        if(v instanceof RadioButton){
            RadioButton rb = (RadioButton) v ;
            rb.setChecked(true);
        }

        transaction = main.getFragmentTransaction();
        int len = frag_main_arr.length;
        for(int i = 0; i < len; i++){
            if(i == clickMenuIndex){
                transaction.show(frag_main_arr[i]);//显示选中的模块
            }else{
                transaction.hide(frag_main_arr[i]);//隐藏其他的模块
            }
        }

        transaction.commit();
        main.closePane();
        main.getTitleTv().setText(arr_title[clickMenuIndex]);
    }
}
