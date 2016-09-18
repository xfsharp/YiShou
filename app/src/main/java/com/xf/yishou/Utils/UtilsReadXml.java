package com.xf.yishou.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xsp on 2016/9/18.
 */
public class UtilsReadXml {
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private boolean isCheck;
    public UtilsReadXml() {

    }

    public UtilsReadXml(SharedPreferences sp, SharedPreferences.Editor spe) {
        this.sp = sp;
        this.spe = spe;
    }

    public void readXml(Context context){
        sp = context.getSharedPreferences("user_xml" , Activity.MODE_PRIVATE);
        isCheck = sp.getBoolean("cb_alogin" , false);
        //未完待续
    }
}
