package com.xf.yishou.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        cv = (CategoryView) view.findViewById(R.id.cv);
        initData();
        return view;
    }

    //获取数据后设置到容器中
    private void initData() {

        XspHttp http = XspHttp.newXspHttp();
        HashMap<String , String > map = new HashMap<String , String >();
        map.put("type" , "1");
        http.getHttpData(UtilsURLPath.getSortPath, "POST", map, new XspHttp.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                Gson gson = new Gson();
                Category[] arr = gson.fromJson(result , Category[].class);
                groupData.addAll(Arrays.asList(arr));
                cv.init(groupData);
            }
        });
    }
}
