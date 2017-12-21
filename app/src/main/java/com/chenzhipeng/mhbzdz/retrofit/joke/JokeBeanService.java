package com.chenzhipeng.mhbzdz.retrofit.joke;

import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface JokeBeanService {
    @GET
    Observable<JokeBean> get(@Url String s);
}
