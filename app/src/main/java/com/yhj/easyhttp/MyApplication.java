package com.yhj.easyhttp;

import android.app.Application;

import com.yhj.network.base.BaseRetrofitHelper;

/**
 * @author : 杨虎军
 * @date :  2021/03/23
 * @desc :
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        BaseRetrofitHelper.init(new INetwork(this));

    }
}
