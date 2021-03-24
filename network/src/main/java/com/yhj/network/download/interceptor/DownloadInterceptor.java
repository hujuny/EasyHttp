package com.yhj.network.download.interceptor;

import androidx.annotation.NonNull;

import com.yhj.network.download.DownloadResponseBody;
import com.yhj.network.download.listener.HttpFileCallBack;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public class DownloadInterceptor implements Interceptor {

    private HttpFileCallBack httpFileCallBack;

    public DownloadInterceptor(HttpFileCallBack httpFileCallBack) {
        this.httpFileCallBack = httpFileCallBack;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), httpFileCallBack))
                .build();
    }
}
