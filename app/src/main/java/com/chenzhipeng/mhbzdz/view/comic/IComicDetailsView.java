package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface IComicDetailsView {
    void setFabVisibility(boolean b);

    void setCollectionStatus(boolean b);

    void setShowProgress(boolean b);

    void setToolbar();

    void onTitleName(String name);

    void onTopImgUrl(String url);

    <T> void onAdapter(T data);

    void onEmptyData();

    void onFail(Throwable e);

    void onCollectionToast(String s);
}
