package com.example.dllo.mirror.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by dllo on 16/6/20.
 */
public abstract class BaseActivity extends AppCompatActivity {
    //绑定视图
    public abstract int mSetContentView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //取消系统title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(mSetContentView());
        initView();
        initData();
    }

    //通过R文件找寻视图
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    //初始化视图
    public abstract void initView();

//    @Override
//    protected void onStart() {
//        super.onStart();
//        initView();
//    }

    //初始化 数据
    public abstract void initData();

//    @Override
//    protected void onResume() {
//        super.onResume();
//        initData();
//    }

}
