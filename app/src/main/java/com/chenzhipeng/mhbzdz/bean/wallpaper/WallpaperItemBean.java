package com.chenzhipeng.mhbzdz.bean.wallpaper;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/18.
 */

public class WallpaperItemBean implements Serializable {
    private String thumb;
    private String img;
    private String localPath;
    private boolean isChecked = false;
    private boolean isShowChecked = false;

    public WallpaperItemBean(String thumb, String img) {
        this.thumb = thumb;
        this.img = img;
    }

    public WallpaperItemBean(String localPath, boolean isChecked, boolean isShowChecked) {
        this.localPath = localPath;
        this.isChecked = isChecked;
        this.isShowChecked = isShowChecked;
    }

    public boolean isShowChecked() {
        return isShowChecked;
    }

    public void setShowChecked(boolean showChecked) {
        isShowChecked = showChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getImg() {
        return img;
    }

    public String getThumb() {
        return thumb;
    }

}
