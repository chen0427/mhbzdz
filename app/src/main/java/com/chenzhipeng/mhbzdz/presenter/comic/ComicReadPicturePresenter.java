package com.chenzhipeng.mhbzdz.presenter.comic;

import com.chenzhipeng.mhbzdz.activity.comic.ComicReadPictureActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicPictureView;


@SuppressWarnings("unchecked")
public class ComicReadPicturePresenter {
    private IComicPictureView pictureView;

    public ComicReadPicturePresenter(ComicReadPictureActivity activity) {
        this.pictureView = activity;
    }

    public void initData() {
        if (ComicReadPictureActivity.type == ComicReadPictureActivity.TYPE_CHAPTER) {
            if (!EmptyUtils.isListsEmpty(ComicReadPictureActivity.dataList)) {
                ComicChapterItemBean bean = ComicReadPictureActivity.dataList.get(ComicReadPictureActivity.position);
                pictureView.onTitle(bean.getComicName());
                pictureView.onData(bean);
            }
        } else if (ComicReadPictureActivity.type == ComicReadPictureActivity.TYPE_DOWNLOAD) {
            if (!EmptyUtils.isListsEmpty(ComicReadPictureActivity.pictures) &&
                    ComicReadPictureActivity.comicIdAndComicName != null &&
                    ComicReadPictureActivity.comicIdAndComicName.length == 3) {
                String comicId = ComicReadPictureActivity.comicIdAndComicName[0];
                String comicName = ComicReadPictureActivity.comicIdAndComicName[1];
                String chapterName = ComicReadPictureActivity.comicIdAndComicName[2];
                pictureView.onTitle(comicName);
                pictureView.onDataToDownload(comicId, comicName, chapterName, ComicReadPictureActivity.pictures);
            }

        }
    }
}
