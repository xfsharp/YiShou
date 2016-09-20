package com.xf.yishou.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xf.yishou.R;
import com.xf.yishou.application.MarketApp;

/**
 * Created by xsp on 2016/9/19.
 */
public class Fragment_UserInfo extends Fragment{
    private View view;
    private TextView tv_user_name_info;
    private TextView tv_user_location;
    private Button butt_info_fix;
    private Button butt_info_exitlogin;

    private CheckBox cb_alogin;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_userinfo , container , false);
        initView();
        setTextView();
        setListener();

        return view;
    }



    /**
     * 设置控件名称
     * */
    private void setTextView() {
        tv_user_name_info.setText(MarketApp.user.getUserName());
        String location = MarketApp.user.getProvince() + " " + MarketApp.user.getCity() + " " + MarketApp.user.getDistrict();
        tv_user_location.setText(location);
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        tv_user_location = (TextView) view.findViewById(R.id.tv_user_location);
        tv_user_name_info  = (TextView) view.findViewById(R.id.tv_user_name_info);
        butt_info_exitlogin = (Button) view.findViewById(R.id.butt_info_exitlogin);
        butt_info_fix = (Button) view.findViewById(R.id.butt_info_fix);

    }

    /**
     * 监听退出和编辑资料按钮
     * */
    private void setListener() {
        //编辑点击监听
        butt_info_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("xsp" , "编辑事件");

            }
        });

        //退出登录监听
        butt_info_exitlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("xsp" , "退出事件");
                MarketApp.user = null;
                getActivity().setResult(getActivity().RESULT_OK);
                /*getActivity().
                        getSharedPreferences("user_xml", getActivity().MODE_PRIVATE).
                        edit().
                        clear().
                        commit();*/
                getActivity().
                        getSharedPreferences("user_xml", getActivity().MODE_PRIVATE).
                        edit().
                        putBoolean("cb_alogin" , false).
                        commit();
                Log.d("xsp" , "配置删除");
                getActivity().finish();
            }
        });
    }
}
