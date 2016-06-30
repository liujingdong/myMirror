package com.example.dllo.mirror.databean;

import android.util.Log;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.allbrowse.AllBrowseAdapter;
import com.example.dllo.mirror.mian.MirrorApp;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.example.dllo.mirror.net.URLValues;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dllo on 16/6/21.
 * 因为就是一个URL,解析一次,然后那里用,就往那里传值
 */
public class ParserDataBean {
    private NetTools netTools ;
    private Gson gson ;

    public ParserDataBean() {
        getBean();
    }

    public void getBean (){
        netTools = new NetTools();
        gson = new Gson();
        //请求体
        String body = "last_time=&device_type=3&page=&token=&version=1.0.1";
        //请求头
        Map<String,String> head = new HashMap<>();
        head.put("Content-Type","application/x-www-form-urlencoded");
        head.put("Content-Length","51");
        head.put("Host","api.mirroreye.cn");
        head.put("Connection","Keep-Alive");
        head.put("Cookie","PHPSESSID=0q0374jcg8m9so617bu65b1qm3; cp_language=zhUser-Agent");
        netTools.getData(URLValues.ALL_URL, new NetListener() {
            @Override
            public void onSuccessed(String string) {
                AllBean allBean = gson.fromJson(string,AllBean.class);//Type1的类
                AllType2Bean allType2Bean = gson.fromJson(string,AllType2Bean.class);//Type2的类
                //通过EventBus发送到自己想用的地方
                EventBus.getDefault().post(allBean);
                EventBus.getDefault().post(allType2Bean);
            }

            @Override
            public void onFailed(VolleyError error) {
            }
        }, head,body);

    }

}
