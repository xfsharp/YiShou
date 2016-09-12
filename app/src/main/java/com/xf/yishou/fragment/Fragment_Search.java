package com.xf.yishou.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xf.yishou.R;
import com.xf.yishou.Utils.UtilsURLPath;
import com.xf.yishou.entity.Category;
import com.xf.yishou.http.XspHttp;
import com.xf.yishou.views.CategoryView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bwfadmin on 2016/9/1.
 */
public class Fragment_Search extends Fragment{
    private List<Category> groupData = new ArrayList<>();
    private CategoryView cv;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        cv = (CategoryView) view.findViewById(R.id.cv);
        initData();
        return view;
    }

    //获取数据后设置到容器中
    private void initData() {

        //首先从SharedPreferences中读取json数据，如果没有，再从网络中请求
        String result = sharedPreferences("httpValue", "read", "Category_data", null);
        if (result != null){
            setData(result);
            return;
        }
        //从网络中请求数据
        XspHttp http = XspHttp.newXspHttp();
        HashMap<String , String > map = new HashMap<String , String >();
        map.put("type" , "1");
        http.getHttpData(UtilsURLPath.getSortPath, "POST", map, new XspHttp.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                sharedPreferences("httpValue" , "write" , "Category_data" , result);
                setData(result);
            }
        });
    }
    //读写SharedPreferences中数据的方法
    private String sharedPreferences(String fileName , String methods , String key , String value){
        sp = getContext().getSharedPreferences(fileName , Activity.MODE_PRIVATE);
        spe = sp.edit();
        if (methods.equals("write")){
            spe.putString(key , value);
        }else if(methods.equals("read")){
            String result = sp.getString(key, value);
            return result;
        }
        spe.commit();
        return null;
    }

    private void setData(String jsonData){
        Gson gson = new Gson();
        Category[] arr = gson.fromJson(jsonData , Category[].class);
        groupData.addAll(Arrays.asList(arr));
        cv.init(groupData);
    }

}
