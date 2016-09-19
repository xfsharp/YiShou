package com.xf.yishou.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xf.yishou.R;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.fragment.Fragment_Login;
import com.xf.yishou.fragment.Fragment_UserInfo;

public class RLoginActivity extends FragmentActivity {

    private Fragment rlogin;
    private Fragment rLogined;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlogin);


        if (MarketApp.user != null){
         // Toast.makeText(getApplicationContext() , "已经登录" , Toast.LENGTH_SHORT).show();
            initLogined();
        }else{
            initLogin();
        }
    }

    /**
     * 加载已经登录界面
     * */
    private void initLogined(){
        rLogined = new Fragment_UserInfo();
        replaceFrag(rLogined);
    }
    /**
     * 加载登录界面
     * */
    private void initLogin() {
        rlogin = new Fragment_Login();
        replaceFrag(rlogin);
    }

    /**
     * 重写返回键
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (MarketApp.user != null || rlogin.isVisible()){
                finish();
            } else {
                replaceFrag(rlogin);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 添加Fragment的方法
     * */
    private void replaceFrag(Fragment frag){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_rlogin, frag).commit();
    }
}
