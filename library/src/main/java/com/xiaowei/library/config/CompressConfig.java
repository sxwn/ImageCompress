package com.xiaowei.library.config;

/**
 * 压缩配置类
 */
public class CompressConfig {
    /**
     * 最小像素不压缩
     */
    private int unCompressMinPixel = 1000;
    /**
     * 标准像素不压缩
     */
    private int unCompressNormalPixel = 2000;
    /**
     * 宽
     */
    private int widthPixel = 40;

    /**
     * 高
     */
    private int heightPixel = 30;
    /**
     * 最大像素
     */
    private int maxPixels = 1200;
    /**
     * 压缩到的最大大小,单位B
     */
    private int maxSize = 200 * 1024;
    /**
     * 是否启用像素压缩
     */
    private boolean enablePixelCompress = true;
    /**
     * 是否启用质量压缩
     */
    private boolean enableQualityCompress = true;
    /**
     * 是否保留源文件
     */
    private boolean enableReserveRaw = true;
    /**
     * 压缩后图片缓存目录，非文件路径
     */
    private String dirCache;
    /**
     * 是否显示压缩进度条
     */
    private boolean showCompressDialog;

    public static CompressConfig getDefaultConfig() {
        return new CompressConfig();
    }

    public CompressConfig() {
    }

    public int getWidthPixel() {
        return widthPixel;
    }

    public void setWidthPixel(int widthPixel) {
        this.widthPixel = widthPixel;
    }

    public int getHeightPixel() {
        return heightPixel;
    }

    public void setHeightPixel(int heightPixel) {
        this.heightPixel = heightPixel;
    }

    public int getUnCompressMinPixel() {
        return unCompressMinPixel;
    }

    public void setUnCompressMinPixel(int unCompressMinPixel) {
        this.unCompressMinPixel = unCompressMinPixel;
    }

    public int getUnCompressNormalPixel() {
        return unCompressNormalPixel;
    }

    public void setUnCompressNormalPixel(int unCompressNormalPixel) {
        this.unCompressNormalPixel = unCompressNormalPixel;
    }

    public int getMaxPixels() {
        return maxPixels;
    }

    public void setMaxPixels(int maxPixels) {
        this.maxPixels = maxPixels;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setEnablePixelCompress(boolean enablePixelCompress) {
        this.enablePixelCompress = enablePixelCompress;
    }

    public boolean isEnableQualityCompress() {
        return enableQualityCompress;
    }

    public void setEnableQualityCompress(boolean enableQualityCompress) {
        this.enableQualityCompress = enableQualityCompress;
    }

    public boolean isEnableReserveRaw() {
        return enableReserveRaw;
    }

    public void setEnableReserveRaw(boolean enableReserveRaw) {
        this.enableReserveRaw = enableReserveRaw;
    }

    public String getDirCache() {
        return dirCache;
    }

    public void setDirCache(String dirCache) {
        this.dirCache = dirCache;
    }

    public boolean isShowCompressDialog() {
        return showCompressDialog;
    }

    public void setShowCompressDialog(boolean showCompressDialog) {
        this.showCompressDialog = showCompressDialog;
    }

    public static class Bulider{
        private CompressConfig config;
        public Bulider(){
            config = new CompressConfig();
        }
        public Bulider setUnCompressMinPixel(int unCompressMinPixel) {
            config.setUnCompressMinPixel(unCompressMinPixel);
            return this;
        }
        public Bulider setUnCompressNormalPixel(int unCompressNormalPixel) {
            config.setUnCompressNormalPixel(unCompressNormalPixel);
            return this;
        }
        public Bulider setMaxSize(int maxSize){
            config.setMaxSize(maxSize);
            return this;
        }
        public Bulider setMaxPixels(int maxPixels){
            config.setMaxPixels(maxPixels);
            return this;
        }
        public Bulider setEnablePixelCompress(boolean enablePixelCompress){
            config.setEnablePixelCompress(enablePixelCompress);
            return this;
        }
        public Bulider setEnableQualityCompress(boolean enableQualityCompress){
            config.setEnableQualityCompress(enableQualityCompress);
            return this;
        }
        public Bulider setEnableReserveRaw(boolean enableReserveRaw){
            config.setEnableReserveRaw(enableReserveRaw);
            return this;
        }
        public Bulider setDirCache(String cacheDir){
            config.setDirCache(cacheDir);
            return this;
        }
        public Bulider setHeightPixel(int heightPixel){
            config.setHeightPixel(heightPixel);
            return this;
        }
        public Bulider setWidthPixel(int widthPixels){
            config.setWidthPixel(widthPixels);
            return this;
        }
        public Bulider setShowCompressDialog(boolean showCompressDialog){
            config.setShowCompressDialog(showCompressDialog);
            return this;
        }
        //构建者
        public CompressConfig create(){
            return config;
        }
    }
}
