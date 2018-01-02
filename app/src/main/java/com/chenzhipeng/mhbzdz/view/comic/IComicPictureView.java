package com.chenzhipeng.mhbzdz.view.comic;


import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;

import java.util.List;

public interface IComicPictureView {

    <T> void onData(T data);

    void onDataToDownload(String comicId, String comicName, String chapterName, List<ComicItemPicture> pictures);

    void onTitle(String s);
}
