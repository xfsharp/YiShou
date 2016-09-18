package com.xf.yishou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.fragment.Fragment_Account;
import com.xf.yishou.fragment.Fragment_Cart;
import com.xf.yishou.fragment.Fragment_Home;
import com.xf.yishou.fragment.Fragment_Menu;
import com.xf.yishou.fragment.Fragment_Search;

public class MainActivity extends FragmentActivity {

    private SlidingPaneLayout slid_pane;
    private RelativeLayout rl_menu;
    private RelativeLayout rl_main;

    private ImageView iv_menu_open;
    private TextView tv_page_tiele;

    private Fragment[] frag_main_arr = new Fragment[4];
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Fragment_Menu frags_menu;

    public TextView tv_unlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();
        initSetting();
        initFrag();
        setListener();
    }


    /**
     * 监听TextView事件
     * */
    private void setListener() {
        tv_unlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , RLoginActivity.class);
                startActivityForResult(intent , 1);
            }
        });
    }

    /**
     * 设置Fragment事务
     * */
    private void initFrag() {
        frag_main_arr[0] = new Fragment_Home();
        frag_main_arr[1] = new Fragment_Account();
        frag_main_arr[2] = new Fragment_Cart();
        frag_main_arr[3] = new Fragment_Search();

        frags_menu = new Fragment_Menu();

        manager = getSupportFragmentManager();

        transaction = getFragmentTransaction();

        int len = frag_main_arr.length;
        for (int i = 0; i < len; i++) {
            transaction.add(R.id.fl_main, frag_main_arr[i]);
            if (i != 0) {
                transaction.hide(frag_main_arr[i]);
            }
        }
        transaction.add(R.id.rl_menu, frags_menu);
        transaction.commit();

    }

    /**
     * 初始化设置
     * */
    private void initSetting() {

        slid_pane.setSliderFadeColor(0);
        iv_menu_open.setOnClickListener(mOnClickLisener);
        //slid_pane.setPanelSlideListener(listener);

    }

    /**
     * 初始化View
     */
    private void initView() {

        slid_pane = (SlidingPaneLayout) findViewById(R.id.slid_pane);
        rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);

        iv_menu_open = (ImageView) findViewById(R.id.iv_menu_open);
        tv_page_tiele = (TextView) findViewById(R.id.tv_page_tiele);

        tv_unlogin = (TextView) findViewById(R.id.tv_unlogin);
    }

    /**
     * 监听iv_menu_open
     */
    View.OnClickListener mOnClickLisener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_menu_open:
                    if (slid_pane.isOpen()) {
                        slid_pane.closePane();
                    } else {
                        slid_pane.openPane();
                    }
                    break;

                default:
                    break;
            }
        }
    };


    /**
     * SlidingPaneLayout的滑动监听
     * 主要监听滑动、打开、关闭菜单的操作
     * 主要实现页面的缩放，先暂时不实现
     * */
  /*  PanelSlideListener listener = new PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {


        }

        @Override
        public void onPanelOpened(View panel) {

        }

        @Override
        public void onPanelClosed(View panel) {

        }
    };*/

    /**
     * 获取主界面Fragment数组
     */
    public Fragment[] getFragArr() {
        return frag_main_arr;
    }

    /**
     * 开启一个事物
     */
    public FragmentTransaction getFragmentTransaction() {
        return manager.beginTransaction();
    }

    /**
     * 获取标题TextView
     */
    public TextView getTitleTv() {
        return tv_page_tiele;
    }

    /**
     * 关闭菜单
     */
    public void closePane() {
        slid_pane.closePane();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            tv_unlogin.setText(MarketApp.user.getUserName());
            frags_menu.tv_user_name.setText(MarketApp.user.getUserName() + "，欢迎回来");
            Log.d("xsp" , "登录成功....");
        }
    }
}
