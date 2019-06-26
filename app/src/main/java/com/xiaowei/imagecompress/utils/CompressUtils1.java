package com.xiaowei.imagecompress.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 普通utils
 */
public class CompressUtils1 {
    /**
     * 压缩图片文件的Bitmap
     *
     * @param filePath 压缩图片文件地址
     * @param quality  要压缩的质量
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int quality) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //计算采样,现在主流手机比较多是800*480px分辨率
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        //用样本大小集解码位图
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        //读取图片角度
        int degree = readPictureDegree(filePath);
        //旋转位图
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private static int readPictureDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i("info","readPictureDegree : orientation = " + orientation);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //Raw height and width of icon
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRadio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRadio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRadio < widthRadio ? widthRadio : heightRadio;
        }
        return inSampleSize;
    }


    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }

}
