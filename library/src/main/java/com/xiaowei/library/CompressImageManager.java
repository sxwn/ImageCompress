package com.xiaowei.library;

import android.content.Context;
import android.text.TextUtils;
import com.xiaowei.library.bean.Photo;
import com.xiaowei.library.config.CompressConfig;
import com.xiaowei.library.core.CompressImageUtil;
import com.xiaowei.library.listener.CompressImage;
import com.xiaowei.library.listener.CompressResultListener;
import java.io.File;
import java.util.ArrayList;

/**
 * 压缩管理类
 */
public class CompressImageManager implements CompressImage {

    private CompressImageUtil compressImageUtil;//压缩工具栏
    private ArrayList<Photo> images;//需要压缩的图片集合
    private CompressImage.CompressListener listener;//压缩监听,首页activity
    private CompressConfig config;//质量配置类

    private CompressImageManager(Context context,CompressConfig config, ArrayList<Photo> images,CompressImage.CompressListener listener){
        compressImageUtil = new CompressImageUtil(context,config);
        this.config = config;
        this.images = images;
        this.listener = listener;
    }

    public static CompressImage build(Context context,CompressConfig config, ArrayList<Photo> images,CompressImage.CompressListener listener){
        return new CompressImageManager(context,config,images,listener);
    }
    //实现
    @Override
    public void compress() {
        //最最核心的代码
        if (images==null || images.isEmpty()){
            listener.onCompressFailed(images,"xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            return;
        }
        for (Photo image:images){
            if (image==null){
                listener.onCompressFailed(images,"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                return;
            }
        }
        //可以压缩,并且从第一张开始压缩
        compress(images.get(0));
    }
    //压缩,从index=1开始压缩
    private void compress(final Photo image) {
        //如果拍照或者相册的源文件为空
        if (TextUtils.isEmpty(image.getOriginalPath())){
            continueCompress(image,false);
            return;
        }
        //源文件判断
        File file = new File(image.getOriginalPath());
        if (!file.exists() ||  !file.isFile()){
            continueCompress(image,false);
            return;
        }
        //需要压缩的图片少于指定的压缩参数 200
        if (file.length()<config.getMaxSize()){
            continueCompress(image,true);//不做压缩,但是是压缩成功
            return;
        }
        //筛选后,最后真正需要压缩
        compressImageUtil.compress(image.getOriginalPath(), new CompressResultListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                //设置压缩后的路径
                image.setCompressPath(imgPath);
                continueCompress(image,true);
            }
            @Override
            public void onCompressFailed(String imgPath, String error) {
                continueCompress(image,false,error);
            }
        });
    }

    /**
     * @param image 需要压缩的图片对象，属性包括:压缩后的图片路径
     * @param isCompressed  是否压缩过
     * @param error 压缩出现了异常
     */
    private void continueCompress(Photo image, boolean isCompressed, String... error) {
        image.setCompressed(isCompressed);
        //获取当前压缩的这张图片对象的索引
        int index = images.indexOf(image);
        //判断是否为需要的压缩图片集合中,最后的一张图片对象
        if (index == images.size()-1){
            //全部压缩完成,通知Activity
            handlerCallBack(error);
        }else{
            //继续压缩,从下一张
            compress(images.get(index+1));
        }
    }

    private void handlerCallBack(String[] error) {
        if (error.length>0){
            listener.onCompressFailed(images,"11111111111111");
        }
        for (Photo image:images){
            //如果存在漏网之鱼
            if (!image.isCompressed()){
                listener.onCompressFailed(images,"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                return;
            }
        }
        listener.onCompressSuccess(images);
    }
}
