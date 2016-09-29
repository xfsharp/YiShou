package com.xf.yishou.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.activity.AddPicActivity;
import com.xf.yishou.application.MarketApp;
import com.xf.yishou.contans.Contans;
import com.xf.yishou.entity.Goods;
import com.xf.yishou.entity.User;
import com.xf.yishou.http.XspHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by xsp on 2016/9/1.
 */
public class Fragment_Cart extends Fragment{
    private View view;
    private LinearLayout ll_hint_login;
    private LinearLayout ll_publish_butt;
    private RelativeLayout rl_publi;
    private ImageView iv_cart_camer;
    private View popSelView;
    private ImageView iv_cart_temp;
    private Button butt_cart_cancel;
    private Button butt_cart_publish;
    private EditText et_publishtitle;
    private EditText ed_publishdespt;
    private EditText et_buy_money;
    private EditText et_wantmoney;
    private TextView tv_now_location;


    private PopupWindow popSelPic;

    private Intent intent;

    public ArrayList<Bitmap> imageList = new ArrayList<>();// 图片列表
    public Goods goods;
    long imageId = 0;

    public LocationClient mLocationClient = null;
    public BDLocationListener mListener = new MyLocationListener();
    public LocationClientOption option;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart,container,false);
        initView();
        setListener();
        initLocation();



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            imageList = data.getParcelableArrayListExtra("bitmap");
        }
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
     * 地图回调接口
     * */

    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null){
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：公里每小时
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());// 单位度
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                String mLocation = location.getAddrStr().toString();
                tv_now_location.setText(mLocation);
            }
        }



    }

    private void initLocation() {

        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener( mListener);

        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
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
                goods = getGoodsFromEdt();
                String info =new Gson().toJson(goods);
                XspHttp upGoodsInfo = XspHttp.newXspHttp();
                HashMap<String , String> map = new HashMap<String , String>();
                map.put("info" , info);
                String url = UtilsURLPath.publishgGoodsPath;
                upGoodsInfo.getHttpData(url , "POST" , map , mUpLoadInfoListener);
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
     * 商品上传信息回调接口
     * */
    XspHttp.OnCompleteListener mUpLoadInfoListener = new XspHttp.OnCompleteListener() {
        @Override
        public void onComplete(String result) {
            if (result != null){
                try {
                    JSONObject obj = new JSONObject(result);
                    int type = obj.getInt("type");
                    if (type == 1){
                        XspHttp xspHttp = XspHttp.newXspHttp();
                        for (int i = 0 ; i < imageList.size() ; i++){
                            Bitmap bitmap = imageList.get(i);
                            xspHttp.upLoadImage("testName" + i , bitmap , UtilsURLPath.uploadPic ,
                                    imageId , mUpLoadImageListener);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 图片上传回调接口
     * */
    XspHttp.OnCompleteListener mUpLoadImageListener = new XspHttp.OnCompleteListener() {
        @Override
        public void onComplete(String result) {
            if (result != null){
                try {
                    JSONObject object = new JSONObject(result);
                    int type = object.getInt("type");
                    if (type == 1){
                        Log.d("xsp" , "图片上传成功");
                    }
                } catch (JSONException e) {
                    Log.d("xsp" , "图片上传返回数据解析异常" + e.toString());
                }
            }
        }
    };

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
            ll_publish_butt.setVisibility(view.VISIBLE);
            ll_hint_login.setVisibility(view.GONE);//隐藏
        }else {
            //未登录
            ll_hint_login.setVisibility(view.VISIBLE);
            rl_publi.setVisibility(view.GONE);
            ll_publish_butt.setVisibility(view.GONE);
        }
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        ll_hint_login = (LinearLayout) view.findViewById(R.id.ll_hint_login);
        ll_publish_butt = (LinearLayout) view.findViewById(R.id.ll_publish_butt);
        rl_publi = (RelativeLayout) view.findViewById(R.id.rl_publi);
        iv_cart_camer = (ImageView) view.findViewById(R.id.iv_cart_camer);
        iv_cart_temp = (ImageView) view.findViewById(R.id.iv_cart_temp);
        butt_cart_cancel = (Button) view.findViewById(R.id.butt_cart_cancel);
        butt_cart_publish = (Button) view.findViewById(R.id.butt_cart_publish);

        et_publishtitle = (EditText) view.findViewById(R.id.et_publishtitle);
        ed_publishdespt = (EditText)view.findViewById(R.id.ed_publishdespt);
        et_buy_money = (EditText)view.findViewById(R.id.et_buy_money);
        et_wantmoney = (EditText)view.findViewById(R.id.et_wantmoney);

        tv_now_location = (TextView) view.findViewById(R.id.tv_now_location);
    }

    /**
     * 需要上传的商品实体类
     * */
    public Goods getGoodsFromEdt() {
        User user = MarketApp.user;
        // 取-->后的二级分类名称: 服饰鞋帽-->上衣
        //String calssify = edtPublishCategory.getText().toString().split("-->")[1];
        String calssify = "冰箱";// 先写一个固定二级分类
        String gName = user.getUserName();

        // 两个一起赋值, 可以不用赋值, 取默认值即可, 由服务器自增生成
        int goodsId = 0;
        int gooldsId = 0;

        // imageId取当前时间的毫秒值, 用来关联商品图片
        imageId = new Date().getTime();
        //imageId = System.currentTimeMillis();

        String goodsName = et_publishtitle.getText().toString();
        String goodsinfo = ed_publishdespt.getText().toString();

        // 两个一起赋值, 也可以传null
        List<String> imagePath = null;
        List<String> imagepath = null;
        // 两个一起赋值
        float originalprice = Float.parseFloat(et_buy_money.getText().toString());
        float Originalprice = originalprice;

        String phone = user.getPhone();
        float price = Float.parseFloat(et_wantmoney.getText().toString());
        String qq = user.getQq();
        int state = 0;// 统一传0,代表商品还在交易期

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(imageId);// 格式化后的时间

        Goods goods = new Goods(calssify, gName, goodsId, goodsName,
                goodsinfo, imagePath, originalprice, phone, price,
                qq, state, time);
        return goods;
    }
}
