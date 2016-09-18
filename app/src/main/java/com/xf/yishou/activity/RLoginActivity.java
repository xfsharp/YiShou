package com.xf.yishou.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xf.yishou.R;
import com.xf.yishou.fragment.Fragment_Login;

public class RLoginActivity extends FragmentActivity {

    private Fragment_Login frag_login;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlogin);

        initFrag();

    }

    /**
     * 加载登录界面
     * */
    private void initFrag() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        frag_login = new Fragment_Login();
        transaction.add(R.id.fl_rlogin , frag_login);
        transaction.commit();
    }
}
