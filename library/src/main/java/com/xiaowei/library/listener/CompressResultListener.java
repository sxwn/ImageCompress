package com.xiaowei.library.listener;

import com.xiaowei.library.bean.Photo;
import java.util.ArrayList;

/**
 * 单张图片压缩时的监听
 */
public interface CompressResultListener {
    //成功
    void onCompressSuccess(String imgPath);
    //失败(异常)
    void onCompressFailed(String imgPath,String error);
}
