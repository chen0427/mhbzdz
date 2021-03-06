package com.chenzhipeng.mhbzdz.retrofit.wallpaper;

import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface WallpaperClassifyBeanService {
    @GET
    Observable<WallpaperClassifyBean> get(@Url String url);
}
