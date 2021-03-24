package com.yhj.network.download.listener;

public interface HttpFileCallBack {

    void onSuccess(String info);

    void onFailure(String error);

    void onProgress(int progress);
}
