package com.yhj.network.util;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpUtil {

    /**
     * Post请求,获取请求体
     *
     * @param params
     * @return
     */
    public static RequestBody getRequestBody(Map<String, Object> params) {
        return getRequestBody(params, MediaType.parse("application/json"));
    }

    public static RequestBody getRequestBody(Map<String, Object> params, MediaType mediaType) {
        JSONObject jsonObject = new JSONObject(params);
        return RequestBody.create(jsonObject.toString(), mediaType);
    }

    /**
     * Post请求,获取请求体
     *
     * @param params
     * @return
     */
    public static RequestBody getRequestBody(String params) {
        return RequestBody.create(params, MediaType.parse("application/json"));
    }


}
