package com.yhj.network.listener;

import android.app.Application;

/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public interface HttpInfo {


    String getAppVersionName();

    String getAppVersionCode();

    boolean isDebug();

    Application getApplicationContext();

}
