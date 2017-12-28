package com.chenzhipeng.mhbzdz.bean.comic;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ComicClassifyBean implements Serializable {
    private String classifyTitle;
    private List<ClassifyType> classifyTypes;
    private boolean isSelect;

    private ComicClassifyBean(boolean isSelect, String classifyTitle, List<ClassifyType> classifyTypes) {
        this.isSelect = isSelect;
        this.classifyTitle = classifyTitle;
        this.classifyTypes = classifyTypes;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getClassifyTitle() {
        return classifyTitle;
    }

    public void setClassifyTitle(String classifyTitle) {
        this.classifyTitle = classifyTitle;
    }

    public List<ClassifyType> getClassifyTypes() {
        return classifyTypes;
    }

    public void setClassifyTypes(List<ClassifyType> classifyTypes) {
        this.classifyTypes = classifyTypes;
    }

    public static class ClassifyType implements Serializable {
        private String title;
        private String tag;

        public ClassifyType(String title, String tag) {
            this.title = title;
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }


    public static List<ComicClassifyBean> getBeanList(String str) {
        List<ComicClassifyBean> beanList = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray array = jsonObject.getJSONArray("comic_sort");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String classifyTitle = object.getString("sort_group");
                        JSONObject data = object.getJSONObject("data");
                        Iterator<String> keys = data.keys();
                        List<ComicClassifyBean.ClassifyType> classifyTypes = new ArrayList<>();
                        while (keys.hasNext()) {
                            String tag = keys.next();
                            String title = data.getString(tag);
                            ComicClassifyBean.ClassifyType classifyType = new ComicClassifyBean.ClassifyType(title, tag);
                            classifyTypes.add(classifyType);

                        }
                        ComicClassifyBean comicClassifyBean = new ComicClassifyBean(i == 0, classifyTitle, classifyTypes);
                        beanList.add(comicClassifyBean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }
}
