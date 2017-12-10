package com.chenzhipeng.mhbzdz.bean.comic;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 章节 (第一章 第二章)
 */
public class ComicChapterItemBean implements Serializable {
    private String comicId;
    private String comicName;
    private String chapterName;
    private String chapterId;
    private String rule;
    private String startNum;
    private String endNum;
    private String chapterDomain;
    private String chapterType;
    private List<ComicItemPicture> comicItemPictureList = new ArrayList<>();
    private boolean isDownLoad = false;
    private boolean isRead = false;
    private boolean isChecked = false;
    private boolean isEnabled = true;

    public ComicChapterItemBean(String comicId, String comicName, String chapterName, String chapterId,
                                String rule, String startNum,
                                String endNum, String chapterDomain, String chapterType,
                                boolean isDownLoad, boolean isRead, boolean isChecked) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterName = chapterName;
        this.chapterId = chapterId;
        this.rule = rule;
        this.startNum = startNum;
        this.endNum = endNum;
        this.chapterDomain = chapterDomain;
        this.chapterType = chapterType;
        this.isDownLoad = isDownLoad;
        this.isRead = isRead;
        this.isChecked = isChecked;
    }

    public List<ComicItemPicture> getComicItemPictureList() {
        if (EmptyUtils.isListsEmpty(comicItemPictureList)) {
            if (!EmptyUtils.isStringsEmpty(startNum, endNum)) {
                int startNumInt = Integer.parseInt(startNum);
                int endNumInt = Integer.parseInt(endNum);
                if (startNumInt != 0 && endNumInt != 0 && !EmptyUtils.isStringsEmpty(rule, chapterName)) {
                    for (int i = 1; i < endNumInt + 1; i++) {
                        String imgHost;
                        if (!TextUtils.isEmpty(chapterDomain)) {
                            imgHost = "http://mhpic." + chapterDomain;
                        } else {
                            imgHost = "http://mhpic.samanlehua.com";
                        }
                        String s = imgHost + rule + "-mht.middle";
                        String currentNumber = String.valueOf(i);
                        String imgUrl = s.replace("$$", currentNumber);
                        ComicItemPicture comicItemPicture = new ComicItemPicture(imgUrl, chapterName, currentNumber, String.valueOf(endNumInt));
                        comicItemPictureList.add(comicItemPicture);
                    }
                }
            }
        }
        return comicItemPictureList;
    }

    public boolean isDownLoad() {
        return isDownLoad;
    }

    public void setDownLoad(boolean downLoad) {
        isDownLoad = downLoad;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getChapterType() {
        return chapterType;
    }

    public void setChapterType(String chapterType) {
        this.chapterType = chapterType;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public String getChapterDomain() {
        return chapterDomain;
    }

    public void setChapterDomain(String chapterDomain) {
        this.chapterDomain = chapterDomain;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }
}
