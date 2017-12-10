package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/27.
 */

public interface IComicTypeView {
    <T> void onAdapter(T data);

    void onTitle(String name);
}
