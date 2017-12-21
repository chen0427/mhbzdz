package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicIntroduceView {
    void onTag(String tagStr, String tagIdStr);

    void onBaseIntroduce(String authorName, String lastChapterName, String desc, String updateTime);
}
