package com.xf.yishou.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xf.yishou.R;
import com.xf.yishou.activity.GoodsListActivity;
import com.xf.yishou.adapter.GridChildAdapter;
import com.xf.yishou.entity.Category;
import java.util.List;

/**
 * Created by xsp on 2016/9/12.
 */
public class CategoryView extends ScrollView{
    private List<Category> groupData;
    private LinearLayout ll_content;
    private GridView gvChild;

    private LayoutInflater inflater;

    private GridChildAdapter adapter;

    private Animation closeAnim;
    private Animation openAnim;

    private int lastPosition = -1;
    private View lastView;

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(final List<Category> groupData){
        this.groupData = groupData;

        inflater = LayoutInflater.from(getContext());
        ll_content = (LinearLayout) inflater.inflate(R.layout.expand_content_layout , null);
        gvChild = (GridView) inflater.inflate(R.layout.child_grid, null);

        this.addView(ll_content);

        openAnim = AnimationUtils.loadAnimation(getContext() , R.anim.arrow_open);
        closeAnim = AnimationUtils.loadAnimation(getContext() , R.anim.arrow_close);

        int num = groupData.size();
        String pckName = getContext().getPackageName();
        Resources res = getResources();

        //循环添加组视图
        for(int i = 0 ;i < num ; i++){
            View groupView = inflater.inflate(R.layout.group_layout , null);
            ImageView iv_left = (ImageView) groupView.findViewById(R.id.iv_left);
            TextView tv_group_name = (TextView) groupView.findViewById(R.id.tv_group_name);

            //添加图片
            int imgId = res.getIdentifier("a" + (i+1), "mipmap" , pckName);
            if (imgId > 0){
                iv_left.setImageResource(imgId);
            }else {
                //随便选的一个
                iv_left.setImageResource(R.drawable.smssdk_sharesdk_icon);
            }

            //设置分类文字
            Category category = groupData.get(i);
            String groupName = category.getSortkey();
            tv_group_name.setText(groupName);

            groupView.setId(i);

            ll_content.addView(groupView);

            //设置分类的点击事件监听
            groupView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    ImageView iv_right = (ImageView) v.findViewById(R.id.iv_right);
                    ImageView right_last = (lastView == null) ? null : (ImageView) lastView.findViewById(R.id.iv_right);

                    ll_content.removeView(gvChild);

                    if (id == lastPosition){
                        iv_right.startAnimation(closeAnim);

                        lastView = null;
                        lastPosition = -1;
                    }else {
                        Category category1 = groupData.get(id);
                        adapter = new GridChildAdapter(category1 , inflater);
                        gvChild.setAdapter(adapter);
                        ll_content.addView(gvChild , (id+1));

                        iv_right.startAnimation(openAnim);
                        if (right_last != null){
                            right_last.startAnimation(closeAnim);
                        }

                        lastPosition = id;
                        lastView = v;
                    }
                }
            });

            //设置子标签内部点击事件监听】
            gvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Category.Secondary secondary = (Category.Secondary) parent.getItemAtPosition(position);
                    String childName = secondary.getSortname();
                    Intent intent = new Intent();
                    intent.setClass(getContext() , GoodsListActivity.class);
                    intent.putExtra("type" , "分类查询");
                    intent.putExtra("conditions" , childName);
                    getContext().startActivity(intent);
                    Toast.makeText(getContext() , "选中的是：" + childName , Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
