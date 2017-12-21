package com.chenzhipeng.mhbzdz.utils;


public class WallpaperApiUtils {

    public static String getBigHot(int skip) {
        return "http://service.picasso.adesk.com/v3/homepage?limit=30&skip=" + skip + "&adult=false&did=352787060712458&first=1&order=hot";
    }

    public static String getHot(int skip) {
        return "http://service.picasso.adesk.com/v1/vertical/vertical?limit=30&skip=" + skip + "&adult=false&first=1&order=hot";
    }

    public static String getBigNew(int skip) {
        return "http://service.picasso.adesk.com/v1/wallpaper/wallpaper?limit=30&skip=" + skip + "&adult=false&first=1&order=new";
    }

    public static String getNew(int skip) {
        return "http://service.picasso.adesk.com/v1/vertical/vertical?limit=30&skip=" + skip + "&adult=false&first=1&order=new";
    }

    public static String getBigClassify() {
        return "http://service.picasso.adesk.com/v1/wallpaper/category?adult=false&first=1";
    }

    public static String getClassify() {
        return "http://service.picasso.adesk.com/v1/vertical/category?adult=false&first=1";
    }

    public static String getBigClassifyNewContent(String id) {
        return "http://service.picasso.adesk.com/v1/wallpaper/category/" + id + "/wallpaper?limit=30&adult=false&first=1&order=new";
    }

    public static String getBigClassifyHotContent(String id) {
        return "http://service.picasso.adesk.com/v1/wallpaper/category/" + id + "/wallpaper?limit=30&adult=false&first=1&order=hot";
    }

    public static String getClassifyHotContent(String id, int skip) {
        return "http://service.picasso.adesk.com/v1/vertical/category/" + id + "/vertical?limit=30&skip=" + skip + "&adult=false&first=1&order=hot";
    }

    public static String getClassifyNewContent(String id, int skip) {
        return "http://service.picasso.adesk.com/v1/vertical/category/" + id + "/vertical?limit=30&skip=" + skip + "&adult=false&first=1&order=new";
    }

    public static String getSearch(String key, int skip) {
        return "http://so.picasso.adesk.com/v1/search/vertical/resource/" + key + "?limit=30&channel=tencent&skip=" + skip + "&adult=false&first=0";
    }

    public static String getBigSearch(String key, int skip) {
        return "http://so.picasso.adesk.com/v1/search/wallpaper/resource/" + key + "?limit=30&channel=tencent&skip=" + skip + "&adult=false&first=0";
    }
}
