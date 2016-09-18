package com.xf.yishou.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.entity.LoginResult;
import com.xf.yishou.entity.User;
import com.xf.yishou.http.XspHttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xsp on 2016/9/7.
 */
public class Fragment_Login extends Fragment{
    private View view;
    private EditText et_user;
    private EditText et_pwd;

    private CheckBox cb_rpwd;
    private CheckBox cb_alogin;
    private Button butt_login;

    private SharedPreferences sf;
    private SharedPreferences.Editor sfe;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rlogin , container , false);
        initView();
        readXml();
        setLoginListener();
        setCheckBoxListener();
        return view;
    }



    @Override
    public void onDestroyView() {

//        if (et_user.getText().equals("") || et_pwd.getText().equals("") ){
//            cb_alogin.setChecked(false);
//            cb_rpwd.setChecked(false);
//        }
//        boolean checked = cb_rpwd.isChecked();
//        if(checked){
//            sf = getContext().getSharedPreferences("user_xml" , Activity.MODE_PRIVATE);
//            sfe = sf.edit();
//            sfe.putBoolean("rpwd_checked" , checked);
//            sfe.commit();
//        }

        super.onDestroyView();
    }

    /**
     * 保存密码和自动登录
     * */
    private void setCheckBoxListener() {
        //监听记住密码复选框的改变
        cb_rpwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_rpwd.isChecked()){
                    update_userXml("cb_rpwd" , true);
                }else {
                    dele_userInfo();
                    update_userXml("cb_rpwd" , false);
                    cb_alogin.setChecked(false);
                }
            }
        });
        //监听自动登录的复选框的改变
        cb_alogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb_alogin.isChecked()){
                    update_userXml("cb_alogin" , true);
                    cb_rpwd.setChecked(true);
                }else{
                    update_userXml("cb_alogin" , false);
                }
            }
        });
    }

    /**
     * 登录按钮的监听
     * */
    private void setLoginListener() {
        butt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = et_user.getText().toString();
                String user_pwd = et_pwd.getText().toString();
                //Log.d("xsp" , "user:" + user + "------->" + "pwd:" + pwd);

                login(user_name , user_pwd);

                if (cb_rpwd.isChecked()){
                    save_userInfo(user_name , user_pwd);
                }

//                et_user.setText("");
//                et_pwd.setText("");
            }
        });
    }

    /**
     * 用户配置信息
     * */
    public void update_userXml(String cb_name , Boolean isChecked){
        sf = getContext().getSharedPreferences("user_xml" , Activity.MODE_PRIVATE);
        sfe = sf.edit();
        sfe.putBoolean(cb_name , isChecked);
        sfe.commit();
        Log.d("xsp" , "用户配置已更新");
    }

    /**
     * 删除用户信息
     * */
    public void dele_userInfo(){
        sf = getContext().getSharedPreferences("user_info" , Activity.MODE_PRIVATE);
        sfe = sf.edit();
        sfe.clear();
        sfe.commit();
        Log.d("xsp" , "删除成功");
    }

    /**
     * 保存用户信息
     * */
    public void save_userInfo(String user_name , String user_pwd){
        sf = getContext().getSharedPreferences("user_info" , Activity.MODE_PRIVATE);
        sfe = sf.edit();
        sfe.putString("user_name", user_name);
        sfe.putString("user_pwd", user_pwd);
        sfe.commit();
        Log.d("xsp" , "保存成功");
    }

    /**
     * 读取配置文件和用户信息
     * */
    public void readXml() {
        sf = getContext().getSharedPreferences("user_xml" , Activity.MODE_PRIVATE);
        boolean rpwd_checked = sf.getBoolean("cb_rpwd", false);
        boolean alogin_checked = sf.getBoolean("cb_alogin", false);
        cb_rpwd.setChecked(rpwd_checked);
        cb_alogin.setChecked(alogin_checked);
        if (rpwd_checked){
            sf = getContext().getSharedPreferences("user_info" , Activity.MODE_PRIVATE);
            String user_name = sf.getString("user_name", null);
            String user_pwd = sf.getString("user_pwd", null);
            et_user.setText(user_name);
            et_pwd.setText(user_pwd);
            if(alogin_checked){
                login(user_name , user_pwd);
            }
        }

    }

    /**
     * 登录方法
     * */
    private void login(String user_name , String user_pwd){
        //将数据获取到的用户数据封装为Json数据
        Gson gson = new Gson();
        User user = new User(user_name,user_pwd);
        String json_user = gson.toJson(user);
        //Log.d("xsp" , json_user);

        Map<String , String> map = new HashMap();
        map.put("user" , json_user);


        //将数据发送到服务端，将接收到的结果发送出去
        String url = UtilsURLPath.userLogin;
        XspHttp.newXspHttp().getHttpData(url ,"POST" , map , new XspHttp.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                Log.d("xsp" , result);
                if (result != null){
                    LoginResult loginResult = new Gson().fromJson(result , LoginResult.class);
                    User resultUser = loginResult.getUser();
                    String type = loginResult.getType();

                    //Log.d("xsp" , type);

                    if ("登录成功".equals(type)){
                        //保存user对象
                        MarketApp.user = resultUser;
                        Intent intent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK , intent);
                        getActivity().finish();
                    }
                }
            }
        });
    }

    /**
     * 初始化View
     * */
    private void initView() {
        et_user = (EditText) view.findViewById(R.id.et_user);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        cb_rpwd = (CheckBox) view.findViewById(R.id.cb_rpwd);
        cb_alogin = (CheckBox) view.findViewById(R.id.cb_alogin);
        butt_login = (Button) view.findViewById(R.id.butt_login);
    }
}
