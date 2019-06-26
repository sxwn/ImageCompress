package com.xiaowei.library.bean;

import java.io.Serializable;

public class Photo implements Serializable {
    private String originalPath;
    private boolean compressed;
    private String compressPath;
    //编辑、网络图片的变更
    private String netImagePath;

    public Photo(String path) {
        this.originalPath = path;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getNetImagePath() {
        return netImagePath;
    }

    public void setNetImagePath(String netImagePath) {
        this.netImagePath = netImagePath;
    }
}
