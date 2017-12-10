package com.chenzhipeng.mhbzdz.bean.comic;


import java.io.Serializable;

/**
 * 漫画单张图片
 */
public class ComicItemPicture implements Serializable {
    private String url;
    private String chapterName;
    private String currentNumber;
    private String endNumber;

    public ComicItemPicture(String url, String chapterName, String currentNumber, String endNumber) {
        this.url = url;
        this.chapterName = chapterName;
        this.currentNumber = currentNumber;
        this.endNumber = endNumber;
    }

    public String getUrl() {
        return url;
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public String getEndNumber() {
        return endNumber;
    }

    public String getChapterName() {
        return chapterName;
    }
}
