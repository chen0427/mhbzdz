package com.chenzhipeng.mhbzdz.presenter.joke;

import com.chenzhipeng.mhbzdz.activity.joke.JokePictureActivity;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokePictureView;


public class JokePicturePresenter {
    private IJokePictureView iJokePictureView;

    public JokePicturePresenter(JokePictureActivity activity) {
        this.iJokePictureView = activity;
    }

    public void initData() {
        if (!EmptyUtils.isListsEmpty(JokePictureActivity.stringList)) {
            iJokePictureView.onData(JokePictureActivity.stringList, JokePictureActivity.readPosition);
        }
    }
}
