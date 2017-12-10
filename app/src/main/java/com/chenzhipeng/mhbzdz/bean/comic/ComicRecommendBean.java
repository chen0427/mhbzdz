package com.chenzhipeng.mhbzdz.bean.comic;

/**
 * Created by Administrator on 2017/8/29.
 */

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 漫画推荐
 */
public class ComicRecommendBean implements Serializable {
    private static ComicRecommendBean comicRecommendBean;
    private List<ComicRecommendTypeBean> typeBeanList;


    private ComicRecommendBean(List<ComicRecommendTypeBean> typeBeanList) {
        this.typeBeanList = typeBeanList;
    }

    public List<ComicRecommendTypeBean> getTypeBeanList() {
        return typeBeanList;
    }

    public static ComicRecommendBean getInstance(String str, boolean isShuffle) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray array = new JSONArray(str);
                List<ComicRecommendTypeBean> typeBeanList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    if (i != 0) {
                        JSONObject object = array.getJSONObject(i);
                        String tabTitle = object.getString("tab_title");
                        JSONArray jsonArray = object.getJSONArray("data");
                        JSONArray slideArray = object.getJSONArray("slide");
                        List<ComicItemBean> itemBeanList = new ArrayList<>();
                        List<ComicRecommendSlideBean> slideBeanList = new ArrayList<>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            String comicId = jsonObject.getString("comic_id");
                            String comicName = jsonObject.getString("comic_name");
                            String lastChapterName = jsonObject.getString("last_chapter_name");
                            ComicItemBean itemBean = new ComicItemBean(comicId, comicName, lastChapterName);
                            itemBeanList.add(itemBean);
                        }

                        for (int j = 0; j < slideArray.length(); j++) {
                            JSONObject slideJsonObject = slideArray.getJSONObject(j);
                            String comicId = slideJsonObject.getString("comic_id");
                            String image = slideJsonObject.getString("image");
                            String slideDesc = slideJsonObject.getString("slide_desc");
                            ComicRecommendSlideBean slideBean = new ComicRecommendSlideBean(comicId, image, slideDesc);
                            slideBeanList.add(slideBean);
                        }
                        ComicRecommendTypeBean recommendTypeBean = new ComicRecommendTypeBean(isShuffle, tabTitle, itemBeanList, slideBeanList);
                        typeBeanList.add(recommendTypeBean);
                    }
                }
                comicRecommendBean = new ComicRecommendBean(typeBeanList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comicRecommendBean;
    }
}
