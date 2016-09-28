package com.xf.yishou.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xf.yishou.activity.MainActivity;
import com.xf.yishou.baidumap.UtilsBaiduLocation;
import com.xf.yishou.entity.User;

import java.util.List;

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
    public static List<Bitmap> bitmap;

    /**
     * 百度地图AK
     */
    public static com.baidu.location.Address address;
    public static com.baidu.location.BDLocation location;

    @Override
    public void onCreate() {
        displayMetrics = new DisplayMetrics();
        context = getApplicationContext();
        res = getResources();
        super.onCreate();
    }
}
