package com.chenzhipeng.mhbzdz.bean.comic;

import java.io.Serializable;

/**
 * 漫画book
 */
public class ComicItemBean implements Serializable {
    private String comicId;
    private String comicName;
    private String lastChapterName;

    private boolean isChecked = false;
    private boolean isShowChecked = false;


    public ComicItemBean(String comicId, String comicName, String lastChapterName) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.lastChapterName = lastChapterName;
    }


    /**
     * 我的书架
     *
     * @param comicId
     * @param comicName
     * @param isShowChecked
     * @param isChecked
     */
    public ComicItemBean(String comicId, String comicName, boolean isShowChecked, boolean isChecked) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.isShowChecked = isShowChecked;
        this.isChecked = isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setShowChecked(boolean showChecked) {
        isShowChecked = showChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isShowChecked() {
        return isShowChecked;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getLastChapterName() {
        return lastChapterName;
    }

    public void setLastChapterName(String lastChapterName) {
        this.lastChapterName = lastChapterName;
    }
}
