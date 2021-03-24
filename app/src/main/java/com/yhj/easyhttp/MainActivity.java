package com.yhj.easyhttp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.yhj.easyhttp.databinding.ActivityMainBinding;
import com.yhj.network.Resource;
import com.yhj.network.listener.HttpCallBack;

/**
 * @author :杨虎军
 * @date :2021/3/23
 * @desc : MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

    public void testUpload(HttpCallBack<LoginEntity> httpCallBack) {
        LiveData<Resource<LoginEntity>> loginLiveData = CommonRetrofitHelper.getService(Api.class).loginLiveData();

        CommonRetrofitHelper.getInstance().doCall(this, loginLiveData, httpCallBack);
    }

    public void test(View view) {
        testUpload(new HttpCallBack<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity content) {
                String json = new Gson().toJson(content);
                Log.e("yhj", "解析: " + json);
            }

            @Override
            public void onFailure(String error) {
//                Log.e("yhj", error.getMessage());
            }
        });

        String savePath = getExternalFilesDir(null).getAbsolutePath();

//        DownloadRetrofitHelper.getInstance().downloadFile(this, "https://console.lytrip.com.cn/wtcp-file/wtcp-material/upload/file/-9999/system/2021/03/19/16/58/1551f06d9c444251ae7ac785bcd80b2a.apk", savePath, "enjoyluoyang.apk", new HttpFileCallBack() {
//            @Override
//            public void onSuccess(String info) {
//                Log.e("yhj", "成功: " + info);
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.e("yhj", "失败: " + error);
//            }
//
//            @Override
//            public void onProgress(int progress) {
//
//                Log.e("yhj", "进度: " + progress);
//            }
//        });
    }
}