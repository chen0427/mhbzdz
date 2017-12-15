package com.chenzhipeng.mhbzdz.bean.comic;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ComicDetailsBean implements Serializable {
    private String comicId;
    private String comicName;
    private String author;
    private String desc;
    private List<String> tagStrList;
    private List<String> tagIdList;
    private String updateTime;
    private List<ComicChapterTypeBean> chapterTypeBeanList;
    private static ComicDetailsBean detailsBean;

    private ComicDetailsBean() {

    }

    public List<String> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(List<String> tagIdList) {
        this.tagIdList = tagIdList;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getTagStrList() {
        return tagStrList;
    }

    public void setTagStrList(List<String> tagStrList) {
        this.tagStrList = tagStrList;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<ComicChapterTypeBean> getChapterTypeBeanList() {
        return chapterTypeBeanList;
    }

    public void setChapterTypeBeanList(List<ComicChapterTypeBean> chapterTypeBeanList) {
        this.chapterTypeBeanList = chapterTypeBeanList;
    }

    public static ComicDetailsBean getInstance(String str, String comicId) {
        if (!EmptyUtils.isStringsEmpty(str, comicId)) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                String comicName = jsonObject.getString("comic_name");
                String comicAuthor = jsonObject.getString("comic_author");
                String comicDesc = jsonObject.getString("comic_desc");
                String updateTime = jsonObject.getString("update_time");
                int comicStatus = jsonObject.getInt("comic_status");
                String chapterType = comicStatus == 2 ?
                        BaseApplication.getContext().getString(R.string.comic_end)
                        : BaseApplication.getContext().getString(R.string.comic_serial);

                detailsBean = new ComicDetailsBean();
                detailsBean.setComicName(comicName);
                detailsBean.setAuthor(comicAuthor);
                detailsBean.setDesc(comicDesc);
                detailsBean.setUpdateTime(updateTime);

                JSONObject comicType = jsonObject.getJSONObject("comic_type");
                Iterator<String> keys = comicType.keys();
                List<String> typeList = new ArrayList<>();
                List<String> tagIdList = new ArrayList<>();
                while (keys.hasNext()) {
                    String next = keys.next();
                    String type = comicType.getString(next);
                    typeList.add(type);
                    tagIdList.add(next);
                }
                detailsBean.setTagStrList(typeList);
                detailsBean.setTagIdList(tagIdList);
                JSONArray comicChapter = jsonObject.getJSONArray("comic_chapter");
                List<ComicChapterTypeBean> chapterBeanList = new ArrayList<>();
                List<ComicChapterItemBean> chapterItemBeanList = new ArrayList<>();
                for (int i = 0; i < comicChapter.length(); i++) {
                    JSONObject object = comicChapter.getJSONObject(i);
                    String chapterName = object.getString("chapter_name");
                    String chapterId = object.getString("chapter_id");
                    String rule = object.getString("rule");
                    String startNum = object.getString("start_num");
                    String endNum = object.getString("end_num");
                    String chapterDomain = object.getString("chapter_domain");
                    ComicChapterItemBean chapterItemBean
                            = new ComicChapterItemBean(comicId, comicName, chapterName, chapterId,
                            rule, startNum, endNum, chapterDomain, chapterType, false, false, false);
                    chapterItemBeanList.add(chapterItemBean);
                }

                if (ConfigUtils.getComicChapterSort() == 0) {
                    //判断设置里面是正确还是倒序
                    Collections.reverse(chapterItemBeanList);
                }

                ComicChapterTypeBean chapterBean = new ComicChapterTypeBean(comicId, comicName, chapterType, chapterItemBeanList, ConfigUtils.getComicChapterSort() == 1);
                chapterBeanList.add(chapterBean);

                detailsBean.setComicId(comicId);
                detailsBean.setChapterTypeBeanList(chapterBeanList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return detailsBean;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }
}
