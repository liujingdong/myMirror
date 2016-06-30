package com.example.dllo.mirror.mian;

import android.app.Application;
import android.content.Context;

import com.example.dllo.mirror.databean.ParserDataBean;

/**
 * Created by dllo on 16/6/20.
 */
public class MirrorApp extends Application {
    //初始化整个应用
    //要去注册的
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
