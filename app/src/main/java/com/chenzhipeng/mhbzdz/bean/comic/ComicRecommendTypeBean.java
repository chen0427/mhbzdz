package com.chenzhipeng.mhbzdz.bean.comic;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public class ComicRecommendTypeBean implements Serializable {
    private String tabTitle;
    private List<ComicItemBean> itemBeanList;
    private List<ComicRecommendSlideBean> slideBeanList;

    public ComicRecommendTypeBean(boolean isShuffle, String tabTitle, List<ComicItemBean> itemBeanList,
                                  List<ComicRecommendSlideBean> slideBeanList) {
        if (isShuffle) {
            Collections.shuffle(itemBeanList);
            Collections.shuffle(slideBeanList);
        }
        this.tabTitle = tabTitle;
        this.itemBeanList = itemBeanList;
        this.slideBeanList = slideBeanList;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public List<ComicItemBean> getItemBeanList() {
        return itemBeanList;
    }

    public List<ComicRecommendSlideBean> getSlideBeanList() {
        return slideBeanList;
    }
}
