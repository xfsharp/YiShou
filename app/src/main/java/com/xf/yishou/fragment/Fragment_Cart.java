package com.xf.yishou.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xf.yishou.R;
import com.xf.yishou.activity.AddPicActivity;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.contans.Contans;

import java.io.File;

/**
 * Created by bwfadmin on 2016/9/1.
 */
public class Fragment_Cart extends Fragment{
    private View view;
    private LinearLayout ll_hint_login;
    private RelativeLayout rl_publi;
    private ImageView iv_cart_camer;
    private View popSelView;
    private ImageView iv_cart_temp;
    private Button butt_cart_cancel;
    private Button butt_cart_publish;

    private PopupWindow popSelPic;

    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart,container,false);
        initView();
        setListener();
        return view;
    }


    /**
     * 启动了其他Activity后, 回到该界面时调用
     * */
    @Override
    public void onResume() {
        super.onResume();
        setShow();
    }

    /**
     * 在该Fragment被hide或者show的时候调用
     * */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            setShow();
        }
    }

    /**
     * 各种监听
     * */
    private void setListener() {
        //拍照图片按钮监听
        iv_cart_camer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopSelWindown();
            }
        });

        //上传按钮监听
        butt_cart_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //取消上传按钮监听
        butt_cart_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 底部弹窗动画
     * */
    private void showPopSelWindown() {
        popSelView = View.inflate(getActivity() , R.layout.pop_view , null);
        Button butt_pop_camera_up = (Button) popSelView.findViewById(R.id.butt_pop_camera_up);
        Button butt_pop_photo_up = (Button) popSelView.findViewById(R.id.butt_pop_photo_up);
        Button butt_pop_cancel_up = (Button) popSelView.findViewById(R.id.butt_pop_cancel_up);

        //设置拍照按钮的监听
        butt_pop_camera_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAnim();
                intent = new Intent();
                intent.setClass(getActivity() , AddPicActivity.class);
                intent.putExtra("selIndex", Contans.REQUESTCODE_TAKE_PICTURE);
                startActivityForResult(intent , Contans.REQUESTCODE_TAKE_PICTURE);
            }
        });

        //设置相册按钮的监听
        butt_pop_photo_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAnim();
                intent = new Intent();
                intent.setClass(getActivity() , AddPicActivity.class);
                intent.putExtra("selIndex" , Contans.REQUESTCODE_ACCESS_ABLUM);
                startActivityForResult(intent ,Contans.REQUESTCODE_ACCESS_ABLUM);
            }
        });

        //设置取消按钮的监听
        butt_pop_cancel_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAnim();
            }
        });

        popSelPic = new PopupWindow(popSelView , view.getWidth() , view.getHeight()/3 , true);

        setTranslationY(popSelView , view.getHeight()/3 ,0);

        popSelPic.showAtLocation(popSelView , Gravity.BOTTOM , 0 , 0);

    }

    /**
     * 取消动画
     * */
    private void cancelAnim(){
        setTranslationY(popSelView , 0 , view.getHeight()/3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popSelPic.dismiss();
                popSelPic = null;
            }
        },1000);
    }

    /**
     * 底部弹出动画效果
     * */
    private void setTranslationY(View popSelView , float ... destY) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(popSelView , "TranslationY" , destY);
        anim.setDuration(1000);
        anim.setInterpolator(new BounceInterpolator());
        anim.start();
    }

    /**
     * 设置界面的显示和隐藏
     * */
    private void setShow() {
        if (MarketApp.user != null){
            //登录成功
            rl_publi.setVisibility(view.VISIBLE);//显示
            ll_hint_login.setVisibility(view.GONE);//隐藏
        }else {
            //未登录
            ll_hint_login.setVisibility(view.VISIBLE);
            rl_publi.setVisibility(view.GONE);
        }
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        ll_hint_login = (LinearLayout) view.findViewById(R.id.ll_hint_login);
        rl_publi = (RelativeLayout) view.findViewById(R.id.rl_publi);
        iv_cart_camer = (ImageView) view.findViewById(R.id.iv_cart_camer);
        iv_cart_temp = (ImageView) view.findViewById(R.id.iv_cart_temp);
        butt_cart_cancel = (Button) view.findViewById(R.id.butt_cart_cancel);
        butt_cart_publish = (Button) view.findViewById(R.id.butt_cart_publish);
    }
}
