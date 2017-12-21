package com.chenzhipeng.mhbzdz.retrofit.joke;

import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface JokeCommentsService {
    @GET
    Observable<NeiHanCommentsBean> get(@Url String url);
}
