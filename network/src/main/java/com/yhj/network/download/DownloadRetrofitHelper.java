package com.yhj.network.download;


import android.annotation.SuppressLint;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.yhj.network.LiveDataCallAdapterFactory;
import com.yhj.network.Resource;
import com.yhj.network.download.interceptor.DownloadInterceptor;
import com.yhj.network.download.listener.DownloadApi;
import com.yhj.network.download.listener.HttpFileCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc : 文件下载
 */
public class DownloadRetrofitHelper {

    private static volatile DownloadRetrofitHelper instance;
    private Retrofit retrofit;

    private DownloadRetrofitHelper() {
    }

    /**
     * DCL单例
     *
     * @return
     */
    public static DownloadRetrofitHelper getInstance() {
        if (instance == null) {
            synchronized (DownloadRetrofitHelper.class) {
                if (instance == null) {
                    instance = new DownloadRetrofitHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 构建Retrofit实例
     *
     * @param httpFileCallBack
     * @return
     */
    private Retrofit getRetrofit(HttpFileCallBack httpFileCallBack) {
        if (retrofit == null) {
            DownloadInterceptor interceptor = new DownloadInterceptor(httpFileCallBack);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .baseUrl("http://www.baidu.com")
                    .build();
        }
        //构建并返回retrofit对象
        return retrofit;
    }

    /**
     * 下载文件
     *
     * @param url              完整url
     * @param dstPath          文件保存位置（绝对路径）
     * @param httpFileCallBack 回调
     */
    public void downloadFile(LifecycleOwner owner, String url, final String dstPath, String child, HttpFileCallBack httpFileCallBack) {


        getRetrofit(httpFileCallBack).create(DownloadApi.class).downloadWithUrl(url).observe(owner, new Observer<Resource<ResponseBody>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged(Resource<ResponseBody> responseBody) {
                if (responseBody.isSuccess()) {
                    ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {

                            File file = writeFile(responseBody.getResource().byteStream(), dstPath, child);
                            if (file != null) {
                                if (responseBody.getResource().contentLength() == file.length()) {
                                    httpFileCallBack.onSuccess("下载成功");
                                } else {
                                    httpFileCallBack.onFailure("下载失败");
                                }
                            } else {
                                httpFileCallBack.onFailure("下载失败");
                            }

                        }
                    });
                } else {
                    httpFileCallBack.onFailure(responseBody.getError());

                }

            }
        });
    }


    /**
     * 写文件到储存
     *
     * @param inputStream
     * @param dstPath
     * @return
     */
    private File writeFile(InputStream inputStream, String dstPath, String child) {
        File file = new File(dstPath, child);
        //如果父目录不存在，先创建目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            fos.flush();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
