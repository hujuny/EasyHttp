package com.yhj.network;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;


/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public class ExceptionHandle {


    public static ResponseException handleException(Throwable e) {
        ResponseException ex;
        if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseException(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseException(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseException(e, ERROR.NETWORD_ERROR);
            ex.message = "服务器无响应或网络异常";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {

            ex = new ResponseException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "服务器无响应或网络异常";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseException(e, ERROR.TIMEOUT_ERROR);
            ex.message = "服务器无响应或网络异常";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseException(e, ERROR.NETWORD_ERROR);
            ex.message = "服务器无响应或网络异常";
            return ex;
        } else {
            Log.e("yhj", "错误" + e);
            ex = new ResponseException(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

    public static class ResponseException extends Exception {
        public int code;
        public String message;

        public ResponseException(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}

