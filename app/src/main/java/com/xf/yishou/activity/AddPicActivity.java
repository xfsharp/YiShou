package com.xf.yishou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.xf.yishou.R;
import com.xf.yishou.adapter.GridAddPicAdapter;
import com.xf.yishou.contans.Contans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddPicActivity extends FragmentActivity {
    private List<Bitmap> model;//M
    private GridView gv_publish;//V
    private GridAddPicAdapter gridAddPicAdapter;//C
    private Bitmap bitmap;

    private ArrayList<String> imagePathList;

    private Button butt_publish_enter;

    private Intent intent;
    private int rel;
    private File tempFile ;
    private static final String TEMP_PHOTO_NAME = "temp_photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        initView();
        initModel();
        setAdapter();
        getRel();
        setListener();
    }

    /**
     * 从相册或相机界面返回后执行
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contans.REQUESTCODE_TAKE_PICTURE){//拍照返回数据
            if (hasSdcard()){
                crop(Uri.fromFile(tempFile));
            }else{
                Toast.makeText(getApplicationContext() , "未找到SD卡，不能储存图片"  , Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == Contans.REQUESTCODE_ACCESS_ABLUM){//相册获取数据
            if (data != null){
                Uri uri = data.getData();
                crop(uri);
            }
        }else if (requestCode == Contans.REQUESTCODE_CROP_CUT){//裁剪图片后的数据
            if (data != null){
                Bitmap bitmap = data.getParcelableExtra("data");
                if (bitmap != null){
                    model.add(model.size() -1 , bitmap);
                    gridAddPicAdapter.notifyDataSetChanged();
                }
            }   try {
                tempFile.delete();
            }   catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 各种监听
     * */
    private void setListener() {
        //监听添加图片的Item
        gv_publish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == model.size() - 1){
                    if (rel == Contans.REQUESTCODE_TAKE_PICTURE){
                        intent = new Intent();
                        intent.setAction("android.media.action.IMAGE_CAPTURE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        if (hasSdcard()){
                            tempFile = new File(Environment.getExternalStorageDirectory() , TEMP_PHOTO_NAME);
                            Uri uri = Uri.fromFile(tempFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT , uri);
                            startActivityForResult(intent , Contans.REQUESTCODE_TAKE_PICTURE);
                        }
                    }else if (rel == Contans.REQUESTCODE_ACCESS_ABLUM){
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent ,Contans.REQUESTCODE_ACCESS_ABLUM);
                    }
                }else {
                    
                }
            }
        });

        //监听确定按钮
        butt_publish_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 获取另外一个Activity中的requestCode
     * */
    private void getRel(){
        intent = getIntent();
        rel = intent.getIntExtra("selIndex", 0);
    }

    /**
     * 绑定适配器
     * */
    private void setAdapter() {
        gridAddPicAdapter = new GridAddPicAdapter(model , getLayoutInflater());
        gv_publish.setAdapter(gridAddPicAdapter);
    }

    /**
     * 初始化数据
     * */
    private void initModel() {
        model = new ArrayList<Bitmap>();
        bitmap = readBmpFromAssets("add_pic.png");
        if (bitmap != null){
            model.add(bitmap);
        }
    }

    /**
     * 通过流将图片转为Bitmap
     * */
    private Bitmap readBmpFromAssets(String fileName) {
        InputStream in = null;
        try {
            in = getAssets().open(fileName);
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化控件
     * */
    private void initView() {
        butt_publish_enter = (Button) findViewById(R.id.butt_publish_enter);
        gv_publish = (GridView) findViewById(R.id.gv_publish);
    }

    /**
     * 判断是否存在SD卡
     * */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 裁剪图片
     * */
    private void crop(Uri uri){
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 180);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为REQUESTCODE_CROP_CUT

        startActivityForResult(intent, Contans.REQUESTCODE_CROP_CUT);
    }
}
