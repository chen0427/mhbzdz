package com.chenzhipeng.mhbzdz.view.joke;

/**
 * Created by Administrator on 2017/8/10.
 */

public interface IJokePictureView {
    <T> void onAdapter(T data, int position);

    void onUpdateBottomBar(String s);

    void start();

    void complete();

    void error(Throwable e);

}
