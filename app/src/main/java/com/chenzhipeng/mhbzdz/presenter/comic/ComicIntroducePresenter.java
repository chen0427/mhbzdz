package com.chenzhipeng.mhbzdz.presenter.comic;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterTypeBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDetailsBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicIntroduceView;

import java.util.List;


public class ComicIntroducePresenter {
    private Fragment fragment;
    private IComicIntroduceView introduceView;

    public ComicIntroducePresenter(Fragment fragment) {
        this.introduceView = (IComicIntroduceView) fragment;
        this.fragment = fragment;
    }

    public void initData() {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            return;
        }
        ComicDetailsBean bean = (ComicDetailsBean) arguments.getSerializable(ComicDetailsActivity.KEY_BUNDLE);
        if (bean == null) {
            return;
        }
        setTag(bean);
        introduceView.onBaseIntroduce(bean.getAuthor(), getLastChapter(bean), bean.getDesc(), bean.getUpdateTime());
    }

    private void setTag(ComicDetailsBean bean) {
        if (bean != null) {
            List<String> tagStrList = bean.getTagStrList();
            List<String> tagIdList = bean.getTagIdList();
            if (!EmptyUtils.isListsEmpty(tagStrList)) {
                for (int i = 0; i < tagStrList.size(); i++) {
                    introduceView.onTag(tagStrList.get(i), tagIdList.get(i));
                }
            }
        }
    }


    private String getLastChapter(ComicDetailsBean bean) {
        String lastChapter = null;
        if (bean != null) {
            List<ComicChapterTypeBean> list1 = bean.getChapterTypeBeanList();
            if (!EmptyUtils.isListsEmpty(list1)) {
                ComicChapterTypeBean typeBean = list1.get(0);
                if (typeBean != null) {
                    List<ComicChapterItemBean> list2 = typeBean.getChapterItemBeanList();
                    if (!EmptyUtils.isListsEmpty(list2)) {
                        ComicChapterItemBean chapterItemBean;
                        if (typeBean.isReverse()) {
                            chapterItemBean = list2.get(0);
                        } else {
                            chapterItemBean = list2.get(list2.size() - 1);
                        }
                        if (chapterItemBean != null) {
                            lastChapter = chapterItemBean.getChapterName();
                        }
                    }
                }
            }
        }
        return lastChapter;
    }
}
