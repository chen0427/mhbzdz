package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IComicDownloadChoiceView {

    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();
}
