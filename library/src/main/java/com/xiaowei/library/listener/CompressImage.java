package com.xiaowei.library.listener;

import com.xiaowei.library.bean.Photo;

import java.util.ArrayList;

/**
 * 图片集合的压缩返回监听
 */
public interface CompressImage {
    //开始压缩(Activity)
    void compress();
    //图片集合的压缩结果返回(notify Activity)
    interface  CompressListener{
        //成功
        void onCompressSuccess(ArrayList<Photo> images);
        //失败(异常)
        void onCompressFailed(ArrayList<Photo> images,String... error);
    }
}
