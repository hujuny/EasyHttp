package com.yhj.easyhttp;

import android.app.Application;

import com.yhj.network.listener.HttpInfo;


/**
 * @author :杨虎军
 * @date :2020/10/10
 * @desc :
 */
public class INetwork implements HttpInfo {
    private Application mApplication;

    public INetwork(Application application) {
        this.mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }
}
