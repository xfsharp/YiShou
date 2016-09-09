package com.xf.yishou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.entity.User;
import com.xf.yishou.http.XspHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

/**
 * Created by xsp on 2016/9/7.
 */
public class Fragment_Register extends Fragment{

    private View view;
    private TextView tv_sms;
    private EditText et_phone;
    private EditText et_sms;
    private EditText et_rgs_user;
    private EditText et_rgs_pwd;
    private EditText et_okpwd;
    private Button butt_register_up;

    private Thread th_countTime;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register , container , false);

        SMSSDK.initSDK(this.getContext() , "16f1cdaa81a8a","1fcbd99a9c4e6a520176eb838086ca8d");
        SMSSDK.registerEventHandler(eh);

        initView();

        setSmsListener();

        setUpListener();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SMSSDK.unregisterEventHandler(eh);
    }

    /**
     * 验证手机短信，并提交用户数据到服务器
     * */
    private void setUpListener() {
        butt_register_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_rgs_pwd.getText().toString();
                String ok_pwd = et_okpwd.getText().toString();

                if (pwd.equals(ok_pwd)){
                    SMSSDK.submitVerificationCode("86" , et_phone.getText().toString() , et_sms.getText().toString());
                }else {
                    et_okpwd.setError("两次密码不一致，请重新输入");
                    et_rgs_pwd.setText(null);
                    et_okpwd.setText(null);
                }

                /**
                 * 无短信验证注册测试
                 * */
                /*String phone = et_phone.getText().toString();
                String user_name = et_rgs_user.getText().toString();

                User user = new User(user_name, pwd , "中国",
                        "四川", "成都", "青阳区", "桐梓林", "13号", phone, "55555");
                Gson gson = new Gson();
                String user_register_json = gson.toJson(user);

                Map<String , String > map = new HashMap<String , String >();
                map.put("user" , user_register_json);

                String url = UtilsURLPath.userRegister;
                XspHttp.newXspHttp().getHttpData(url, "POST", map, new XspHttp.OnCompleteListener() {
                    @Override
                    public void onComplete(String result) {
                        Log.d("xsp" , result);
                    }
                });*/
            }
        });
    }

    /**
     * 获取短信验证码
     * */
    private void setSmsListener() {

        tv_sms.setOnClickListener(new View.OnClickListener() {
            String frag = null;
            @Override
            public void onClick(View v) {
                String url = UtilsURLPath.checkPhoneNum;
                String rgs_user = et_phone.getText().toString();
                Map<String , String> map = new HashMap<String , String >();
                map.put("phoneNum" , rgs_user);
                XspHttp.newXspHttp().getHttpData(url, "GET", map, new XspHttp.OnCompleteListener() {
                    @Override
                    public void onComplete(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String type = jsonObject.getString("type");
                            String content = jsonObject.getString("content");
                            if(type.equals("1")){

                                //SMSSDK.getVerificationCode("86",et_phone.getText().toString());
                                setIsEnable(false);
                                tv_sms.setText("等待验证短信码");
                                if (th_countTime == null){
                                    th_countTime = new Thread(new CountRun());
                                    th_countTime.start();
                                }else {
                                    new Thread(new CountRun()).start();
                                }

                                //Log.d("xsp" , content);
                            }else {
                                et_phone.setError(content);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    /**
     *设置控件能否被点击
     * */
    private void setIsEnable(boolean isEnable) {
        et_phone.setEnabled(isEnable);
    }

    /**
     * 短信回调接口对象
     * */
    EventHandler eh = new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    String phone = et_phone.getText().toString();
                    String user_name = et_rgs_user.getText().toString();
                    String pwd = et_rgs_pwd.getText().toString();

                    User user = new User(user_name, pwd , "中国",
                            "四川", "成都", "青阳区", "桐梓林", "13号", phone, "55555");
                    Gson gson = new Gson();
                    String user_register_json = gson.toJson(user);

                    Map<String , String > map = new HashMap<String , String >();
                    map.put("user" , user_register_json);

                    String url = UtilsURLPath.userRegister;
                    XspHttp.newXspHttp().getHttpData(url, "POST", map, new XspHttp.OnCompleteListener() {
                        @Override
                        public void onComplete(String result) {


                            Log.d("xsp" , result);
                        }
                    });
                    //Log.d("xsp" , "验证成功");
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
//                    Looper.prepare();
//                    Toast.makeText(getContext() , "发送验证码成功" , Toast.LENGTH_LONG).show();
//                    Looper.loop();
                    Message msg = msghandler.obtainMessage();
                    msg.what = 0;
                    msghandler.sendMessage(msg);
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };
    /**
     * 初始化控件
     * */
    private void initView() {
        tv_sms = (TextView) view.findViewById(R.id.tv_sms);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_sms = (EditText) view.findViewById(R.id.et_sms);
        et_rgs_user = (EditText) view.findViewById(R.id.et_rgs_user);
        et_rgs_pwd = (EditText) view.findViewById(R.id.et_rgs_pwd);
        et_okpwd = (EditText) view.findViewById(R.id.et_okpwd);
        butt_register_up = (Button) view.findViewById(R.id.butt_register_up);
    }

    /**
     * 等待短信验证的时间线程
     * */
    class CountRun implements Runnable {
        int time = 60;
        @Override
        public void run() {

            while(time > 0){
                time--;
                final StringBuffer count = new StringBuffer();
                if(time < 10){
                    count.append("0" + time + "秒后再获取");
                }else {
                    count.append(time + "秒后再获取");
                }
                tv_sms.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_sms.setText(count.toString());
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tv_sms.post(new Runnable() {
                @Override
                public void run() {
                    tv_sms.setText("获取短信验证码");
                    setIsEnable(true);
                }
            });
        }
    }

    private Handler msghandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getContext() , "发送验证码成功" ,Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
}
