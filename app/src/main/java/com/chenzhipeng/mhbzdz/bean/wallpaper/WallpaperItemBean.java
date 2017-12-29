package com.chenzhipeng.mhbzdz.bean.wallpaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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




    public static List<WallpaperItemBean> getWallpaperItemList(WallpaperBean bean) {
        List<WallpaperItemBean> beanList = new ArrayList<>();
        if (bean != null) {
            List<WallpaperBean.Res.Vertical> verticals = bean.getRes().getVerticals();
            if (verticals != null && verticals.size() > 0) {
                for (WallpaperBean.Res.Vertical v : verticals) {
                    WallpaperItemBean itemBean = new WallpaperItemBean(v.getThumb(), v.getImg());
                    beanList.add(itemBean);
                }
            }
        }
        return beanList;
    }


    public static List<WallpaperClassifyItemBean> getWallpaperClassifyItemList(WallpaperClassifyBean bean) {
        List<WallpaperClassifyItemBean> beanList = new ArrayList<>();
        if (bean != null) {
            List<WallpaperClassifyBean.Res.Classify> classifies = bean.getRes().getClassifies();
            if (classifies != null && classifies.size() > 0) {
                for (WallpaperClassifyBean.Res.Classify c : classifies) {
                    WallpaperClassifyItemBean itemBean =
                            new WallpaperClassifyItemBean(c.getName(), c.getId(), c.getCover());
                    beanList.add(itemBean);
                }
            }
        }
        return beanList;
    }



}
