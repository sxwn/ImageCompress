package com.xiaowei.library.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class CommonUtils{
    /**
     * PDA设备
     * 许多定制的Android系统并不带相机功能，如果强行调用,程序会崩溃
     * @param activity
     * @param intent
     * @param requestCode
     */
    public static void hasCamera(Activity activity,Intent intent,int requestCode){
        if (activity==null){
            throw new IllegalArgumentException("Activity为空");
        }
        PackageManager packageManager = activity.getPackageManager();
        boolean hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Camera.getNumberOfCameras()>0;
        if (hasCamera) {
            activity.startActivityForResult(intent, requestCode);
        }else {
            Toast.makeText(activity,"当前设备没有相机",Toast.LENGTH_LONG).show();
            throw new IllegalArgumentException("当前设备没有相机");
        }
    }

    /**
     * 获取拍照intent
     * @param outPutUri
     * @return
     */
    public static Intent  getCameraIntent(Uri outPutUri){
        //调用图库,获取所有本地图片
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outPutUri);//将获取到的图片保存到指定URI
        return intent;
    }

    /**
     * 跳转到图库
     * @param activity
     * @param requestCode
     */
    public static void openAlbum(Activity activity,int requestCode){
        //调用图库,获取所有本地图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 显示圆形进度对话框
     * @param activity
     * @param progressTitle
     * @return
     */
    public static ProgressDialog showProgressDialog(Activity activity,String... progressTitle){
        if (activity==null || activity.isFinishing()) return null;
        String title = "提示";
        if (progressTitle!=null && progressTitle.length>0)title = progressTitle[0];
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}