package com.yhj.network.download;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yhj.network.download.listener.HttpFileCallBack;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private HttpFileCallBack httpFileCallBack;

    public DownloadResponseBody(ResponseBody responseBody, HttpFileCallBack callBackLis) {
        this.responseBody = responseBody;
        this.httpFileCallBack = callBackLis;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(BufferedSource source) {
        return new ForwardingSource(source) {

            long totalBytesRead = 0L;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
                final int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                if (httpFileCallBack != null) {
                    //回调分发到主线程
                    new Handler(Looper.getMainLooper()).post(() -> httpFileCallBack.onProgress(progress));
                }
                return bytesRead;
            }
        };
    }


}
