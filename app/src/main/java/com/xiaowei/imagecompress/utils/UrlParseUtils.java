package com.xiaowei.imagecompress.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.xiaowei.imagecompress.MainActivity;

import java.io.File;

public class UrlParseUtils {
    /**
     * 创建一个图片文件输出路径的Uri(FileProvider)
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file){
        return FileProvider.getUriForFile(context,getFileProvider(context),file);
    }

    /**
     * 获取FileProvider路径,过滤6.0+
     * @param context
     * @return
     */
    private static String getFileProvider(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * 获取拍照后图片的Uri
     * @param context
     * @param cacheFile
     * @return
     */
    public static Uri getCameraOutPutUri(Context context,File cacheFile){
        return getUriForFile(context,cacheFile);
    }

    public static String getPath(Context context, Uri uri) {
        return uri.getPath();
    }
}
