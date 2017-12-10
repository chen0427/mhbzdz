package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/24.
 */

public interface IComicIntroduceView {
    void onTag(String tagStr, String tagIdStr);

    void onBaseIntroduce(String authorName, String lastChapterName, String desc);
}
