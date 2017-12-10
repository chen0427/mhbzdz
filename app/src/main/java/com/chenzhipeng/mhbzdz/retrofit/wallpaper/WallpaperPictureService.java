package com.chenzhipeng.mhbzdz.retrofit.wallpaper;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/11/20.
 */

public interface WallpaperPictureService {
    @GET
    Observable<ResponseBody> get(@Url String url);
}
