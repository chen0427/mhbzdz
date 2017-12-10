package com.chenzhipeng.mhbzdz.bean.comic;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ComicRecommendSlideBean implements Serializable {
    private String comicId;
    private String imgUrl;
    private String slideDesc;

    public ComicRecommendSlideBean(String comicId, String imgUrl, String slideDesc) {
        this.comicId = comicId;
        this.imgUrl = imgUrl;
        this.slideDesc = slideDesc;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSlideDesc() {
        return slideDesc;
    }

    public void setSlideDesc(String slideDesc) {
        this.slideDesc = slideDesc;
    }
}
