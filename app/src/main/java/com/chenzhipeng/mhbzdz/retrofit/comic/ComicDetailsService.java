package com.chenzhipeng.mhbzdz.retrofit.comic;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface ComicDetailsService {
    @GET
    Observable<ResponseBody> get(@Url String url);
}