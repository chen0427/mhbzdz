package com.chenzhipeng.mhbzdz.utils;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ComicApiUtils {
    public static String getRecommend() {
        return "https://api.yyhao.com/mht_api/v2/getrecommend/?client-channel=tencent&client-version=1.1.1&client-type=android";
    }

    public static String getComicImg(String comicId) {
        return "http://image.samanlehua.com/mh/" + comicId + ".jpg";
    }

    public static String getClassify() {
        return "https://api.yyhao.com/mht_api/v2/getconfig/?client-channel=tencent&client-version=1.1.1&client-type=android";
    }

    public static String getClassifyImg(String tag) {
        return "http://image.samanlehua.com/file/kanmanhua_images/sort/" + tag + ".png";
    }

    public static String getDetails(String comicId) {
        return "https://getcomicinfo-globalapi.yyhao.com/app_api/v5/getcomicinfo/?comic_id=" + comicId + "&platformname=android&productname=mht";

        //  return "https://api.yyhao.com/mht_api/v2/getcomicinfo/?comic_id=" + comicId + "&client-channel=tencent&client-version=1.1.1&client-type=android";
    }


    public static String getType(String tag, String orderby, int page) {
        return "https://api.yyhao.com/mht_api/v2/getsortlist/?comic_sort=" + tag + "&page=" + page + "&orderby=" + orderby + "&search_key=&client-channel=tencent&client-version=1.1.1&client-type=android";
    }


    public static String getSearch(String searchKey, String orderby, int page) {
        return "https://api.yyhao.com/mht_api/v2/getsortlist/?comic_sort=&page=" + page + "&orderby=" + orderby + "&search_key=" + searchKey + "&client-channel=tencent&client-version=1.1.1&client-type=android";
    }
}
