package com.chenzhipeng.mhbzdz.document;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.FileUtils;

import java.io.File;

/**
 * 本地漫画操作
 */
public class ComicDocumentHelper {
    private static final String SPLIT = "-";

    private static volatile ComicDocumentHelper instance;

    private ComicDocumentHelper() {
    }

    public static ComicDocumentHelper getInstance() {
        if (instance == null) {
            synchronized (ComicDocumentHelper.class) {
                if (instance == null) {
                    instance = new ComicDocumentHelper();
                }
            }
        }
        return instance;
    }


    private String getNameToUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            String[] split = url.split(File.separator);
            int length = split.length;
            String s = split[length - 3] + split[length - 2] + split[length - 1];
            return s.replace(".jpg-mht.middle", "");
        }
        return null;
    }

    public String getStoragePath(String url, String comicId, String comicName, String chapterName) {
        if (!EmptyUtils.isStringsEmpty(url, comicId, comicName, chapterName)) {
            return BaseApplication.COMIC_PATH + File.separator + comicId + SPLIT + comicName + File.separator + chapterName + File.separator + getNameToUrl(url) + ".jpg";
        }
        return null;
    }


    private String getStringId(int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }

    /**
     * 根据本地的文件配置 ComicDownloadBean
     *
     * @param comicDownloadBean
     * @return
     */
    public ComicDownloadBean getDataToStorage(ComicDownloadBean comicDownloadBean) {
        if (comicDownloadBean != null) {
            String comicId = comicDownloadBean.getComicId();
            String comicName = comicDownloadBean.getComicName();
            String chapterName = comicDownloadBean.getChapterName();
            String path = BaseApplication.COMIC_PATH + File.separator + comicId + SPLIT + comicName + File.separator + chapterName;
            File file = new File(path);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    int progress = 0;
                    for (File f : files) {
                        if (f.exists() && f.isFile()) {
                            progress++;
                        }
                    }
                    comicDownloadBean.setProgress(progress);
                    comicDownloadBean.setPage(comicDownloadBean.getProgress() + "/" + comicDownloadBean.getMaxProgress());
                    if (progress == comicDownloadBean.getMaxProgress()) {
                        comicDownloadBean.setState(getStringId(R.string.complete_download));
                    } else if (0 < progress && progress < comicDownloadBean.getMaxProgress()) {
                        comicDownloadBean.setState(getStringId(R.string.pause_download));
                    }
                }
            }
        }
        return comicDownloadBean;
    }


    public boolean deleteDownloadData(ComicDownloadBean comicDownloadBean) {
        if (comicDownloadBean != null) {
            String comicId = comicDownloadBean.getComicId();
            String comicName = comicDownloadBean.getComicName();
            String chapterName = comicDownloadBean.getChapterName();
            String path = BaseApplication.COMIC_PATH + File.separator + comicId + SPLIT + comicName + File.separator + chapterName;
            File file = new File(path);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        if (f.exists()) {
                            boolean b = f.delete();
                            if (!b) {
                                return false;
                            }
                        }
                    }
                }
                return file.delete();
            }
        }
        return false;
    }


    public void deleteBook(String comicId, String comicName) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName)) {
            String path = BaseApplication.COMIC_PATH + File.separator + comicId + SPLIT + comicName;
            File file = new File(path);
            FileUtils.deleteDir(file);
        }
    }
}
