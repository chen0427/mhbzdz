package com.chenzhipeng.mhbzdz.download;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 漫画下载管理
 */
public class ComicDownloaderManager {
    private static volatile ComicDownloaderManager instance;
    private Map<String, ComicDownloadBean> comicDownloadBeanMap = new HashMap<>();

    private ComicDownloaderManager() {

    }

    public static ComicDownloaderManager getInstance() {
        if (instance == null) {
            synchronized (ComicDownloaderManager.class) {
                if (instance == null) {
                    instance = new ComicDownloaderManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加下载任务
     */
    public void put(ComicDownloadBean comicDownloadBean) {
        if (comicDownloadBean != null) {
            comicDownloadBeanMap.put(comicDownloadBean.getComicId() + comicDownloadBean.getChapterName(), comicDownloadBean);
        }
    }

    /**
     * 移除下载任务
     */
    public void remove(ComicDownloadBean comicDownloadBean) {
        if (comicDownloadBean != null) {
            comicDownloadBeanMap.remove(comicDownloadBean.getComicId() + comicDownloadBean.getChapterName());
        }
    }


    public void remove(String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            Set<String> strings = comicDownloadBeanMap.keySet();
            for (String s : strings) {
                if (s.contains(comicId)) {
                    ComicDownloadBean comicDownloadBean = comicDownloadBeanMap.get(s);
                    comicDownloadBean.pause();
                    comicDownloadBean.delete();
                    comicDownloadBeanMap.remove(s);
                }
            }
        }

    }

    public ComicDownloadBean get(String comicId, String chapterName) {
        return comicDownloadBeanMap.get(comicId + chapterName);
    }


}
