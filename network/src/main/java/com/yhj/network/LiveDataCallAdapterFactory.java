package com.yhj.network;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author : 杨虎军
 * @date :  2021/03/23
 * @desc :
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@Nullable Type returnType, @Nullable Annotation[] annotations, @Nullable Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            throw new IllegalArgumentException("返回值不是LiveData类型");
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("Response must be parametrized as " +
                    "LiveData<Resource> or LiveData<? extends Resource>");
        }

        //先解释一下getParameterUpperBound
        //官方例子
        //For example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
        //获取的是Map<String,? extends Runnable>参数列表中index序列号的参数类型,即0为String,1为Runnable
        //这里的0就是LiveData<?>中?的序列号,因为只有一个参数
        //其实这个就是我们请求返回的实体

        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        if (getRawType(responseType) == Response.class) {
            if (!(responseType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parametrized as " +
                        "LiveData<Response<Resource>> or LiveData<Response<? extends Resource>>");
            }

            return new LiveDataCallAdapter<>(responseType);
        } else if (getRawType(responseType) == Resource.class) {
            if (!(responseType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parametrized as " +
                        "LiveData<Resource> or LiveData<? extends Resource>");
            }
            return new LiveDataCallAdapter<>(getParameterUpperBound(0, (ParameterizedType) responseType));

        } else {
            return null;
        }
    }

}
