package com.chenzhipeng.mhbzdz.bean.comic;

import java.io.Serializable;
import java.util.List;

/**
 * 章节类型 (连载 番外)
 */
public class ComicChapterTypeBean implements Serializable {
    /**
     * 是否倒序
     */
    private boolean isReverse = true;
    private String chapterType;
    private List<ComicChapterItemBean> chapterItemBeanList;
    private String comicId;
    private String comicName;

    public ComicChapterTypeBean(String comicId, String comicName, String chapterType, List<ComicChapterItemBean> chapterItemBeanList, boolean isReverse) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterType = chapterType;
        this.chapterItemBeanList = chapterItemBeanList;
        this.isReverse = isReverse;
    }

    /**
     * 是否倒序
     *
     * @return
     */
    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
    }

    public String getChapterType() {
        return chapterType;
    }

    public List<ComicChapterItemBean> getChapterItemBeanList() {
        return chapterItemBeanList;
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
}
