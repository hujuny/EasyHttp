package com.yhj.network;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : 杨虎军
 * @date :  2021/03/23
 * @desc :
 */
public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<Resource<T>>> {

    private Type responseType;

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<Resource<T>> adapt(final Call<T> call) {
        final MutableLiveData<Resource<T>> liveDataResponse = new MutableLiveData<>();
        call.enqueue(new LiveDataResponseCallCallback(liveDataResponse));
        return liveDataResponse;
    }


    private static class LiveDataResponseCallCallback<T> implements Callback<T> {
        private final MutableLiveData<Resource<T>> liveData;

        LiveDataResponseCallCallback(MutableLiveData<Resource<T>> liveData) {
            this.liveData = liveData;
        }

        @Override
        public void onResponse(Call<T> call, @NotNull Response<T> response) {
            if (call.isCanceled()) {
                return;
            }
            if (response.isSuccessful()) {
                liveData.postValue(Resource.success(response.body()));
            } else {
                switch (response.code()) {
                    case UNAUTHORIZED:
                    case FORBIDDEN:
                    case NOT_FOUND:
                    case REQUEST_TIMEOUT:
                    case GATEWAY_TIMEOUT:
                    case INTERNAL_SERVER_ERROR:
                    case BAD_GATEWAY:
                    case SERVICE_UNAVAILABLE:
                    default:
                        liveData.postValue(Resource.error("服务器无响应或网络异常"));
                        break;
                }

            }
        }

        @Override
        public void onFailure(Call<T> call, @NotNull Throwable t) {

            if (call.isCanceled()) {
                return;
            }
            ExceptionHandle.ResponseException exception = ExceptionHandle.handleException(t);

            liveData.postValue(Resource.error(exception.getMessage()));
        }
    }
}
