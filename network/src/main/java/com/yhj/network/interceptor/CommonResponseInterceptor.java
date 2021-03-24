package com.yhj.network.interceptor;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : yhj
 * @date :  2020/10/10
 * @desc :token失效拦截
 */
public class CommonResponseInterceptor implements Interceptor {


    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}

