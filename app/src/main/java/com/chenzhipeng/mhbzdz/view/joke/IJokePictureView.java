package com.chenzhipeng.mhbzdz.view.joke;


public interface IJokePictureView {
  /*  <T> void onAdapter(T data, int position);

    void onUpdateBottomBar(String s);

    void start();

    void complete();

    void error(Throwable e);*/

    <T> void onData(T data, int position);
}
