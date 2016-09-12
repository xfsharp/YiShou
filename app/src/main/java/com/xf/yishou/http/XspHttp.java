package com.xf.yishou.http;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xsp on 2016/9/2.
 *
 * 封装的框架中应满足以下功能：
 *  a)	可以进行POST和GET请求
 *  b)	网络请求的操作应当封装在子线程中
 *  c)  网络请求结束后应该有回调
 *  d)	应该能够直接在回调方法中更新UI
 *  e)	线程应该使用线程池进行管理
 *  f)	框架的工具类应该是单例模式
 */
public class XspHttp {
    private static XspHttp xspHttp;//单例模式的唯一实例
    private static ExecutorService mService;//线程池
    private static final int WHAT = 0;//消息标识常量
    //private static OnCompleteListener mListener;//回调接口
    private HashMap<Integer , OnCompleteListener> listenerMap;
    private int key = -1;

    private XspHttp(){

    }

    //获取XspHttp对象唯一的方法
    public static XspHttp newXspHttp(){
        if(xspHttp == null){
            synchronized (XspHttp.class) {
                if(xspHttp == null){
                    xspHttp = new XspHttp();
                    mService = Executors.newFixedThreadPool(5);// 初始化线程池
                }
            }
        }
        return xspHttp;
    }

    /**
     * 获取网络数据的方法
     *	String url, 网络请求接口地址
     * 	String method, 请求方法,比如POST,GET等
     * 	Map map, 请求参数
     * 	OnCompleteListener mListener, 请求结束的回调
     * */
    public void getHttpData(String url, String method, Map<String ,String> map,OnCompleteListener mListener){

        if(listenerMap == null ){
            listenerMap = new HashMap<>();
        }
        listenerMap.put(++key , mListener);
        mService.submit(new HttpRunnable(url , method , map , key));
    }

    /**
     * 网络请求线程
     * */
    private class HttpRunnable implements Runnable{
        private String url;
        private String method;
        private Map<String,String> map;
        private int key;

        public HttpRunnable(String url, String method, Map<String, String> map , int key) {
            super();
            this.key = key;
            this.url = url;
            this.method = method;
            this.map = map;
        }

        @Override
        public void run() {
            URL path = null;
            HttpURLConnection conn = null;
            BufferedReader br = null;
            BufferedWriter bw = null;
            int code = -1;

            Message msg = Message.obtain();
            msg.what = WHAT;
            msg.arg1 = key;

            try {
                if(Method.GET.equals(method)) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(url).append("?");
                    for (Map.Entry entry : map.entrySet()){
                        buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    buffer.deleteCharAt(buffer.length()-1);
                    path = new URL(buffer.toString());
                    conn = (HttpURLConnection) path.openConnection();
                    conn.setRequestMethod(method);
                }else if (Method.POST.equals(method)){
                    StringBuffer buffer = new StringBuffer();
                    for (Map.Entry entry : map.entrySet()){
                        buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    buffer.deleteCharAt(buffer.length()-1);
                    path = new URL(url);
                    conn = (HttpURLConnection) path.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                    bw.write(buffer.toString());
                    bw.flush();
                }

                code = conn.getResponseCode();
                if(code == 200){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer result = new StringBuffer();
                    String line = null;
                    while((line = br.readLine()) != null){
                        result.append(line);
                    }
                    msg.obj = result.toString();
                }else {
                    msg.obj = null;
                    Log.d("xsp", "请求失败:返回码："+code);
                }
            }catch (Exception e) {
                e.printStackTrace();
                msg.obj = null;
                Log.d("xsp", "请求失败:返回码："+e.toString());
            }finally {
                try {
                    mHandler.sendMessage(msg);
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 网络请求的两种方法 存在BUG，未启用
     * */
   /* private static StringBuffer httpPut(String method,URL path,HttpURLConnection conn,BufferedReader br,BufferedWriter bw,int code,
                                 String url,Map<String,String> map) {
        StringBuffer buffer = new StringBuffer();
        if (Method.GET.equals(method)) {
            buffer.append(url).append("?");
        }
        for (Map.Entry entry : map.entrySet()) {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        StringBuffer result = null;
        try {
            if (Method.GET.equals(method)) {
                path = new URL(buffer.toString());
            } else {
                path = new URL(url);
            }
            conn = (HttpURLConnection) path.openConnection();
            if (Method.GET.equals(method)) {
                conn.setRequestMethod(method);
            } else {
                conn.setDoInput(true);
                conn.setDoInput(true);
                bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                bw.write(buffer.toString());
                bw.flush();
            }

            code = conn.getResponseCode();
            if (code == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            return result;
        }

    }*/

    /**
     * 两种方法名的内部类
     * */
    public static class Method{
        public static final String POST = "POST";
        public static final String GET = "GET";
    }
    /**
     * 回调接口方法
     * */
    public interface OnCompleteListener{
        void onComplete(String result);
    }

    /**
     * 消息发送方法
     * */
    private void sendMsg(String result) {
        Message msg = Message.obtain();
        msg.what = WHAT;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    /**
     * 回调消息
     * */
    private	Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    OnCompleteListener mListener = listenerMap.get(msg.arg1);
                    if(mListener != null){// 请求结束,收到消息后调用回调函数
                        mListener.onComplete(msg.obj.toString());
                    }
                    break;

                default:
                    break;
            }
        };
    };

    public void clearRequest(){
        listenerMap.clear();
        listenerMap = null;
    }
}
