package com.chenzhipeng.mhbzdz.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface NetworkService {
    @GET
    Observable<ResponseBody> get(@Url String url);
}
