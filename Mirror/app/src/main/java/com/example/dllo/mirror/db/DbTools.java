package com.example.dllo.mirror.db;

import com.litesuits.orm.LiteOrm;

import java.util.List;

/**
 * Created by dllo on 16/6/20.
 */
public class DbTools {
    private LiteOrm liteOrm = DbSingleton.getInstance().getLiteOrm();

    //往数据库里面插入数据
    public <T> void insert(Class<T> tClass) {
        liteOrm.insert(tClass);
    }

    //删除全部
    public <T> void deleteAll(List<Class<T>> list) {
        liteOrm.delete(list);
    }

    //删除指定单位
    public <T> void delete(Class<T> tClass) {
        liteOrm.delete(tClass);
    }

    //查询数据
    public <T> void query(Class<T> tClass) {
        liteOrm.query(tClass);
    }
}
