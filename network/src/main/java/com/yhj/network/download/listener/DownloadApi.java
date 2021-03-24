package com.yhj.network.download.listener;


import androidx.lifecycle.LiveData;

import com.yhj.network.Resource;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc : download api
 */
public interface DownloadApi {

    /**
     * 下载文件
     *
     * @param url 完整的url
     * @return
     */
    @Streaming
    @GET
    LiveData<Resource<ResponseBody>> downloadWithUrl(@Url String url);
}
