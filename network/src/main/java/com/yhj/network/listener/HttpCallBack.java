package com.yhj.network.listener;

/**
 * @author :杨虎军
 * @date :2020/10/31
 * @desc : 网络请求回调监听
 */
public interface HttpCallBack<T> {

    void onSuccess(T content);
//    void onCacheSuccess(T content);

    void onFailure(String error);

}
