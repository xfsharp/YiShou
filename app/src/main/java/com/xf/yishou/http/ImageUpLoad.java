package com.xf.yishou.http;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xsp on 2016/9/22.
 */

public class ImageUpLoad implements Runnable{
    private String ImageName;
    private Bitmap bitmap;
    private String path;
    private Long imageId;

    public ImageUpLoad(String imageName, Bitmap bitmap, String path, Long imageId) {
        ImageName = imageName;
        this.bitmap = bitmap;
        this.path = path;
        this.imageId = imageId;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

            DataOutputStream imageOut = new DataOutputStream(conn.getOutputStream());

            // 先把图片的一些基本信息,比如名字,id等,写入到服务器端
            imageOut.writeBytes(
                    "--*****\r\nContent-Disposition: form-data; " + "name=\""
                            + imageId + "\";filename=\"" + ImageName + "\"" + "\r\n\r\n");

            // bitmap 转 流, 把Bitmap压缩到一个输出流当中
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // bitmap 压缩到字节数组流中
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , bos);
            InputStream imageIn = new ByteArrayInputStream(bos.toByteArray());

            int bufferSzie = 2048;
            byte[] buffer = new byte[bufferSzie];
            int length = -1;
            while ((length = imageIn.read(buffer)) != -1){
                imageOut.write(buffer , 0 , length);
            }

            // 告诉服务器,图片数据写完了
            imageOut.writeBytes("\r\n--******--\r\n");

            //关流
            imageIn.close();
            imageOut.flush();

            // 读服务器返回的结果, 取得Response内容
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuffer sBuffer = new StringBuffer();
            while ((line = br.readLine()) != null){
                sBuffer.append(line);
            }

            imageOut.close();
        } catch (Exception e) {
            String result = "上传失败：" + e.toString();
            Log.d("xsp" , result);
        }
    }
}
