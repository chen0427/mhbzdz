package com.chenzhipeng.mhbzdz.retrofit.wallpaper;

import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface WallpaperBeanService {
    @GET
    Observable<WallpaperBean> get(@Url String url);
}
