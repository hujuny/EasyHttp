package com.yhj.network.base;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.yhj.network.LiveDataCallAdapterFactory;
import com.yhj.network.Resource;
import com.yhj.network.cache.CacheManager;
import com.yhj.network.environment.EnvironmentActivity;
import com.yhj.network.environment.HttpEnvironment;
import com.yhj.network.interceptor.CommonRequestInterceptor;
import com.yhj.network.interceptor.CommonResponseInterceptor;
import com.yhj.network.listener.HttpCallBack;
import com.yhj.network.listener.HttpInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author : yhj
 * @date :  2020/10/30
 * @desc :Retrofit
 */
public abstract class BaseRetrofitHelper implements HttpEnvironment {

    private Retrofit.Builder retrofitBuilder;
    public static String BASE_URL;

    private static boolean mIsFormal = true;
    private static HttpInfo iNetworkRequiredInfo;
    private final CommonRequestInterceptor requestInterceptor;


    public Retrofit getRetrofit() {
        return getRetrofit(null);
    }

    public Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit;
        if (TextUtils.isEmpty(baseUrl)) {
            retrofit = retrofitBuilder.baseUrl(BASE_URL).build();
        } else {
            retrofit = retrofitBuilder.baseUrl(baseUrl).build();
        }
        return retrofit;
    }


    public static void init(HttpInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
        mIsFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
    }


    public BaseRetrofitHelper() {

        if (!mIsFormal) {
            BASE_URL = getTest();
        } else {
            BASE_URL = getFormal();
        }


        //构建OkHttpClient对象
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (getInterceptor() != null) {
            builder.addInterceptor(getInterceptor());
        }
        //日志拦截器
        if (iNetworkRequiredInfo != null && (iNetworkRequiredInfo.isDebug())) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            builder.addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        requestInterceptor = new CommonRequestInterceptor(iNetworkRequiredInfo);
        builder.addInterceptor(requestInterceptor)
                .addInterceptor(new CommonResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        OkHttpClient okHttpClient = builder.build();

        //创建retrofit构建者对象
        retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory());
    }


    public <T> void doCall(LifecycleOwner owner, LiveData<Resource<T>> liveData, HttpCallBack<T> httpCallBack, String... key) {

        if (httpCallBack == null) {
            throw new IllegalArgumentException("HttpCallBack为空");
        }

        //观察者_网络请求状态
        //缓存

        liveData.observe(owner, tResource -> {
            try {
                if (tResource.getResource() != null) {
                    if (tResource.isSuccess()) {
                        httpCallBack.onSuccess(tResource.getResource());

                        if (key.length > 0) {
                            for (String s : key) {
                                CacheManager.save(s, tResource.getResource());
                            }
                        }
                    } else {
                        if (key.length > 0) {
                            for (String s : key) {
                                Object cache = CacheManager.getCache(s);
                                T localCache = (T) cache;
                                if (localCache != null) {
                                    httpCallBack.onSuccess(localCache);
                                } else {
                                    httpCallBack.onFailure(tResource.getError());
                                }
                            }
                        } else {
                            httpCallBack.onFailure(tResource.getError());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (key.length > 0) {
                    for (String s : key) {
                        Object cache = CacheManager.getCache(s);
                        T localCache = (T) cache;
                        if (localCache != null) {
                            httpCallBack.onSuccess(localCache);
                        } else {
                            httpCallBack.onFailure(e.getMessage());
                        }
                    }
                } else {
                    httpCallBack.onFailure(e.getMessage());
                }
            }
        });
    }


    /**
     * 自定义拦截器
     */
    protected abstract Interceptor getInterceptor();

}
