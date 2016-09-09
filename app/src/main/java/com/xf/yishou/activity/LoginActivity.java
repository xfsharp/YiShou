package com.xf.yishou.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xf.yishou.R;
import com.xf.yishou.fragment.Fragment_Login;
import com.xf.yishou.fragment.Fragment_Register;

public class LoginActivity extends FragmentActivity {


    private LinearLayout ll_login_back;
    private TextView tv_login_title;

    private Fragment_Login frag_login;
    private Fragment_Register frag_register;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private TextView tv_register_butt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initFrag();
        setOnRegisterListener();
        setOnBackListener();
    }

    private void setOnBackListener() {
        ll_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frag_login.isVisible()){
                    finish();
                }else if (frag_register.isVisible()){
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl_login,frag_login);
                    transaction.commit();
                    tv_login_title.setText("登录界面");
                    tv_register_butt.setText("注册");
                    setOnRegisterListener();
                }
            }
        });
    }



    private void setOnRegisterListener() {
        tv_register_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                frag_register = new Fragment_Register();
                transaction.replace(R.id.fl_login,frag_register);
                transaction.commit();
                tv_login_title.setText("注册界面");
                tv_register_butt.setText("");
            }
        });
    }

    /**
     * 初始化登录Fragment
     * */
    private void initFrag() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        frag_login = new Fragment_Login();
        transaction.add(R.id.fl_login,frag_login);
        transaction.commit();
    }

    /**
     * 初始化View
     * */
    private void initView() {
        tv_login_title = (TextView) findViewById(R.id.tv_login_title);
        ll_login_back = (LinearLayout)findViewById(R.id.ll_login_back);
        tv_register_butt = (TextView) findViewById(R.id.tv_register_butt);
    }

    /**
     * 重写返回键
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if(frag_login.isVisible()){
                finish();
            }else if (frag_register.isVisible()){
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.fl_login,frag_login);
                transaction.commit();
                tv_login_title.setText("登录界面");
                tv_register_butt.setText("注册");
                setOnRegisterListener();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
