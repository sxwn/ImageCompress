package com.xiaowei.library.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import com.xiaowei.library.config.CompressConfig;
import com.xiaowei.library.listener.CompressResultListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 压缩图片核心工具类
 */
public class CompressImageUtil {

    private CompressConfig config;
    private Context context;
    private Handler handler = new Handler();

    public CompressImageUtil( Context context, CompressConfig config){
        this.context = context;
        this.config = config==null?CompressConfig.getDefaultConfig():config;
    }

    public void compress(String imgPath, CompressResultListener listener){
        if (config.isEnableQualityCompress()){
            compressImageByPixel(imgPath,config.getWidthPixel(),config.getHeightPixel(),listener);
        }else{
            compressImageByQuality(BitmapFactory.decodeFile(imgPath),imgPath,listener);
        }
    }

    private void compressImageByPixel(String imgPath,int width,int height,CompressResultListener listener) {
        int maxPixels = this.config.getMaxPixels();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = height;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = width;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
    }

    private void compressImageByQuality(Bitmap bitmap, String imgPath, CompressResultListener listener) {
        if (bitmap ==null){
            return;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > config.getMaxSize()) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        // Generate compressed image file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imgPath);
            fos.write(os.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
