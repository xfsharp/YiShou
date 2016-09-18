package com.xf.yishou.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.xf.yishou.activity.MainActivity;
import com.xf.yishou.entity.User;

/**
 * Created by xsp on 2016/9/18.
 */
public class MarketApp extends Application {
    public static Context context;
    public static String pckName;
    public static MainActivity mainActivity;
    public static Resources res;
    public static DisplayMetrics displayMetrics;

    public static User user;

    @Override
    public void onCreate() {
        displayMetrics = new DisplayMetrics();
        context = getApplicationContext();
        res = getResources();
        super.onCreate();
    }
}
