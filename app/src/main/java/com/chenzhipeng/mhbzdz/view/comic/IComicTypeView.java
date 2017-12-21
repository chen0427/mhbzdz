package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicTypeView {
    <T> void onAdapter(T data);

    void onTitle(String name);
}
