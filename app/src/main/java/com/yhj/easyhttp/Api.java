package com.yhj.easyhttp;

import androidx.lifecycle.LiveData;

import com.yhj.network.Resource;

import retrofit2.http.GET;

public interface Api {

    @GET("skydriver/preview?access_token=a92b9c50-a42d-4fdb-8466-507d34b0bd59&fileUri=https://www.yanghujun.com/upload/2019/12/heart-5f900bb03cab4755bbac1c7124b03bc8.jpg")
    LiveData<Resource<LoginEntity>> loginLiveData();
}
