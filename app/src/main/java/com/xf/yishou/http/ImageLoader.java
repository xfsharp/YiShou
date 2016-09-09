package com.xf.yishou.http;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.xf.yishou.Utils.UtilsURLPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xsp on 2016/9/6.
 * 1.定义框架工具类
 * 2.实现单例模式
 * 3.创建线程池
 * 4.实现缓存策略
 * 5.定义缩放图片的方法
 * 6.实现子线程更新UI的方式
 * 7.封装请求方法
 * 8.定义图片加载的线程类
 */
public class ImageLoader {
    private static ImageLoader mImgLoader;
    private ExecutorService mService;
    private LruCache<String,Bitmap> mLruCache;
    private String sdPath;
    /**
     * 初始化
     * */
    private ImageLoader() {
        mService = Executors.newFixedThreadPool(5);
        //获取运行内存最大容量
        Long maxSize = Runtime.getRuntime().maxMemory();
        //占用内存1/4
        mLruCache = new LruCache<String , Bitmap>((int)(maxSize/4));

        sdPath = Environment.getExternalStorageDirectory().getPath()+ File.separator + "MyCache";
        File dir = new File(sdPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    /**
     * 单例模式唯一创建对象的方法
     * */
    public static ImageLoader newInstance(){
        if(mImgLoader == null){
            synchronized(ImageLoader.class){
                if (mImgLoader == null){
                    mImgLoader = new ImageLoader();
                }
            }
        }
        return mImgLoader;
    }

    /**
     * 获取图片的方法
     * */
    public void LoadImage(String fileName , ImageView targetView){
        Bitmap bmp = null;
        targetView.setTag(fileName);
        //LruCache中取得图片
        bmp = mLruCache.get(fileName);
        if(bmp != null){
            setBitmap(bmp , targetView , fileName);
            return;
        }

        //从SD卡中读取图片
        String path = sdPath + File.separator + fileName;
        bmp = BitmapFactory.decodeFile(path);
        if (bmp != null){
            setBitmap(bmp , targetView , fileName);
            mLruCache.put(fileName , bmp);
            return;
        }

        //从网络获取图片
        LoadRun run = new LoadRun(fileName , targetView);
        mService.submit(run);
        //new Thread(run).start();

    }

    /**
     * 将图片设置到UI界面
     * */
    private void setBitmap(final Bitmap bmp, final ImageView targetView, String fileName) {
        String tag = targetView.getTag().toString();
        if (tag == null || !tag.equals(fileName)){
            return;
        }

        Activity act = null;
        act = (Activity) (targetView.getContext());
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                targetView.setImageBitmap(bmp);
            }
        });
    }

    class LoadRun implements Runnable{
        private String fileName;
        private ImageView targetView;

        public LoadRun(String fileName, ImageView targetView) {
            super();
            this.fileName = fileName;
            this.targetView = targetView;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            URL url = null;
            String base = UtilsURLPath.downloadPic;
            String path = base + fileName;

            try {
                url = new URL(path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();

                if (code == 200){
                    InputStream in = conn.getInputStream();
                    Bitmap bmp = BitmapFactory.decodeStream(in);
                    setBitmap(bmp , targetView , fileName);

                    //将图片存进LruCache
                    mLruCache.put(fileName , bmp);

                    //将图片存进SD卡
                    String imgPath = sdPath + File.separator + fileName;
                    FileOutputStream fos = new FileOutputStream(imgPath);
                    bmp.compress(Bitmap.CompressFormat.JPEG , 100 , fos);
                   // Log.d("xsp" ,"请求成功" );
                }else {
                    Log.d("xsp" , "返回码：" + code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
