package com.yhj.network.interceptor;


import androidx.annotation.NonNull;

import com.yhj.network.listener.HttpInfo;
import com.yhj.network.util.TimeUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : yhj
 * @date :  2020/10/31
 * @desc :公用的头部内容
 */
public class CommonRequestInterceptor implements Interceptor {


    private HttpInfo requiredInfo;
    private String url;

    public CommonRequestInterceptor(HttpInfo requiredInfo) {
        this.requiredInfo = requiredInfo;
    }

    String date = TimeUtils.timeStamp2Date("yyyy-MM-dd HH:mm:ss");

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        url = chain.request().url().toString();
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("os", "android");
        builder.addHeader("appVersion", requiredInfo.getAppVersionCode());
        builder.addHeader("date", date);
        return chain.proceed(builder.build());

    }

    public String getUrl() {
        return url;
    }


}

