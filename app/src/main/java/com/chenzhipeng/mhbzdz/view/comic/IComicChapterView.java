package com.chenzhipeng.mhbzdz.view.comic;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

/**
 * Created by Administrator on 2017/9/19.
 */

public interface IComicChapterView {
    /**
     * 更新章节类型状态栏 (番外,连载)
     *
     * @param view
     * @param textView
     */
    void addChapterType(View view, AppCompatTextView textView);


    <T> void onAdapter(T data);

    /**
     * 更改章节类型按钮状态
     *
     * @param position
     */
    void updateBarForLeft(int position);

    /**
     * 更改章节顺序按钮状态
     *
     * @param isReverse
     */
    void updateBarForRight(boolean isReverse);

    void setFabText(String s);

}
