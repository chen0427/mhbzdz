package com.chenzhipeng.mhbzdz.bean.wallpaper;

import java.io.Serializable;


public class WallpaperClassifyItemBean implements Serializable {
    private String id;
    private String name;
    private String cover;

    public WallpaperClassifyItemBean(String name, String id, String cover) {
        this.name = name;
        this.id = id;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
