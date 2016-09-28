package com.xf.yishou.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.xf.yishou.R;
import com.xf.yishou.activity.MainActivity;

/**
 * Created by xsp on 2016/9/26.
 * 测试服务
 */

public class TestServices extends Service{
    private MyBind mBind = new MyBind();

    @Override
    public void onCreate() {
        Notification.Builder builder = new Notification.Builder(getApplication());
        builder.setContentInfo("补充内容");
        builder.setContentText("测试通知内容");
        builder.setContentTitle("这是一条通知消息");
        builder.setSmallIcon(R.mipmap.call);
        builder.setTicker("新消息");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        Intent intent = new Intent(getApplication() , MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication() , 0 , intent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1 , notification);
        Log.d("xsp" , "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("xsp" , "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("xsp" , "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    public class MyBind extends Binder{
        public void down(){
            Log.d("xsp" , "下载任务");
        }
    }
}
