package com.xiaowei.imagecompress;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xiaowei.library.CompressImageManager;
import com.xiaowei.library.bean.Photo;
import com.xiaowei.library.config.CompressConfig;
import com.xiaowei.library.listener.CompressImage;
import com.xiaowei.library.utils.CachePathUtils;
import com.xiaowei.imagecompress.utils.UrlParseUtils;
import com.xiaowei.library.utils.CommonUtils;
import com.xiaowei.library.utils.Constants;
import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 实际项目使用场景:
 * 1、能否后台上传原图，让后台处理?   --->除非是工业级项目
 * 2、图片服务器的磁盘空间非常昂贵,比如七牛云、阿里云
 * 3、尽可能避免安卓OOM异常
 * 4、后台约定的规则,比如每张图片必须<=300kb  flie.length
 * 朋友圈上传图片的压缩步骤:   4.4以上可以并发压缩
 * 1、递归压缩每张图片(兼容)  2、设置图片格式   3、质量压缩   4、像素压缩   5、返回压缩结果集   6、完成压缩
 * 2、设置图片格式:
 * 常用格式: .png .peng  .webp    Bitmap.CompressFormat.JPEG
 * 3、质量压缩
 * width*height,一个像素的所占用的字节数计算,宽高不变
 * bitmap.compress(format,quality,baos),由于png是无损压缩,所以设置quality无效(不适合做缩略图)
 * 4、采样率压缩
 * 缩小图片分辨率,减少所占用磁盘空间和内存大小  BitmapFactory.Options.inSampleSize
 * 5、缩放压缩
 * 减少图片的像素,降低所占用磁盘空间大小和内存大小  canvas.drawBitmap(bitmap,null,recF,null) [native]  可以用于缓存缩略图
 * 6、JNI调用JPEG库
 * Android的图片引擎使用的是阉割版的skia引擎,去掉了图片压缩中的哈夫曼算法
 * https://www.jianshu.com/p/14874817eb4b
 */

public class MainActivity extends AppCompatActivity implements CompressImage.CompressListener {

    private ProgressDialog dialog;//压缩dialog
    private String cameraCachePath;//拍照后,源文件路径
    private CompressConfig compressConfig;//压缩配置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //运行时权限申请 23  6.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] perms = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,163);
            }
        }
//        compressConfig = CompressConfig.getDefaultConfig();
//        CompressConfig.Bulider bulider = new CompressConfig.Bulider();
//        bulider.setDirCache().create();
        compressConfig = new CompressConfig.Bulider().
                setUnCompressMinPixel(1000)//最小像素不压缩,默认是:1000
                .setUnCompressNormalPixel(2000)//标准像素不压缩,默认是2000
                .setMaxPixels(1200)//长或宽不超过的最大像素.默认是2000
                .setMaxSize(100*1024)//压缩到的最大大小
                .setEnablePixelCompress(true)
                .setEnableQualityCompress(true)
                .setEnableReserveRaw(true)
                .setDirCache("")
                .setShowCompressDialog(true)
                .create();
//        testLuban();
    }
    private void testLuban() {
        String mCacheDir = Constants.BASE_CACHE_PATH + getPackageName() + "/cache/" + Constants.COMPRESS_CACHE;
        Log.e("weip",mCacheDir);
        //Luban内部采用IO线程进行图片压缩，外部调用只需设置好结果监听即可
        Luban.with(this)
                .load("/storage/emulated/0/DCIM/Camera/IMG_20190322_142010.jpg")//原图地址
                .ignoreBy(100)//如果图片<=100kb不启用压缩
                .setTargetDir(mCacheDir)//压缩后存放地址
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif");
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.e("weip","onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.e("weip",file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("weip",e.getMessage());
                    }
                }).launch();
    }
    //拍照点击事件
    public void camera(View view) {
        //7.0 FileProvider(指定拍完照之后的源文件路径)
        Uri outputUri;
        File file = CachePathUtils.getCameraCacheFile();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0+
            outputUri = UrlParseUtils.getCameraOutPutUri(this,file);
        }else{
            outputUri = Uri.fromFile(file);
        }
        //拍照后的源文件路径
        cameraCachePath = file.getAbsolutePath();
        //启动拍照
        CommonUtils.hasCamera(this, CommonUtils.getCameraIntent(outputUri),Constants.CAMERA_CODE);
    }
    //相册点击事件
    public void album(View view) {
        CommonUtils.openAlbum(this,Constants.ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照后的返回
        if (requestCode == Constants.CAMERA_CODE && resultCode == RESULT_OK){
            //开始压缩
            proComgress(cameraCachePath);
        }
        //相册返回
        if (requestCode == Constants.ALBUM_CODE && resultCode == RESULT_OK){
            if (data!=null){
                Uri uri = data.getData();
                String path = UrlParseUtils.getPath(this,uri);
                //开始压缩
                proComgress(path);
            }
        }
    }
    private void proComgress(String path) {
        //集合批量压缩
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo(path));
        if (!photos.isEmpty())
            compress(photos);//开始压缩
    }
    //最终交给压缩引擎,等待返回
    private void compress(ArrayList<Photo> photos) {
        if (compressConfig.isShowCompressDialog()){
            Log.e("weip>>>","开始压缩");
            dialog = CommonUtils.showProgressDialog(this,"开始压缩");
        }
        //压缩启动
        CompressImageManager.build(this,compressConfig,photos,this).compress();
    }
    //返回监听
    @Override
    public void onCompressSuccess(ArrayList<Photo> images) {
        Log.e("weip>>>","压缩成功");
        if (dialog!=null)dialog.dismiss();
    }

    @Override
    public void onCompressFailed(ArrayList<Photo> images, String... error) {
        Log.e("weip>>>","压缩失败");
        if (dialog!=null)dialog.dismiss();
    }
}
