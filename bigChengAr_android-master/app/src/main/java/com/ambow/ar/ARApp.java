package com.ambow.ar;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.baidu.ar.bean.DuMixARConfig;
import com.baidu.ar.util.Res;

import java.util.LinkedList;

public class ARApp extends Application {
    public static LinkedList<Activity> activityList = new LinkedList<>();
    public static String indexUrl  = "https://www.ambow.com/Reporthall/";
    public static String userUrl  = "https://www.ambow.com/Reporthall/userInformation.html";
    public static String indexUrl_f  = "file:///android_asset/web/index.html";
    @Override
    public void onCreate() {
        super.onCreate();
        Res.addResource(this);
        // 设置App Id
        DuMixARConfig.setAppId("15673629");
        // 设置API Key
        DuMixARConfig.setAPIKey("SLjdpVzfuQ0gEsWN0xhtL0HK");
        // 设置Secret Key
        DuMixARConfig.setSecretKey("bCj8FE2KK46kGHjRlRPqcw5uwgODaqVh ");


    }
}