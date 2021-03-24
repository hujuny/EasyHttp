package com.yhj.network;

import androidx.annotation.Nullable;

/**
 * @author yhj
 */
public class Resource<T> {
    private T resource;
    private String error;

    private Resource() {
    }

    public boolean isSuccess() {
        return resource != null && error == null;
    }

    @Nullable
    public T getResource() {
        return resource;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public static <T> Resource<T> success(@Nullable T body) {
        final Resource<T> resource = new Resource<>();
        resource.resource = body;
        return resource;
    }

    public static <T> Resource error(@Nullable String error) {
        final Resource<T> resource = new Resource<>();
        resource.error = error;
        return resource;
    }
}
