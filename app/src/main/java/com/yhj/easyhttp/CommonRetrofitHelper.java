package com.yhj.easyhttp;


import android.text.TextUtils;

import com.yhj.network.base.BaseRetrofitHelper;

import okhttp3.Interceptor;

/**
 * @author : yhj
 * @date :  2020/10/10
 * @desc :
 */
public class CommonRetrofitHelper extends BaseRetrofitHelper {

    private static volatile CommonRetrofitHelper instance;


    public static CommonRetrofitHelper getInstance() {
        if (instance == null) {
            synchronized (CommonRetrofitHelper.class) {
                if (instance == null) {
                    instance = new CommonRetrofitHelper();
                }
            }
        }
        return instance;
    }

    public static <T> T getService(Class<T> service) {
        return getService(service, null);
    }

    public static <T> T getService(Class<T> service, String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            return getInstance().getRetrofit().create(service);
        } else {
            return getInstance().getRetrofit(baseUrl).create(service);
        }

    }


    @Override
    protected Interceptor getInterceptor() {
        return null;
    }


    @Override
    public String getFormal() {
        return "http://open.zhxc.fzyzxxjs.cn/api/";
    }

    @Override
    public String getTest() {
        return "";
    }


}