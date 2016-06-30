package com.example.dllo.mirror.db;

import com.example.dllo.mirror.mian.MirrorApp;
import com.litesuits.orm.LiteOrm;

/**
 * Created by dllo on 16/6/20.
 */
public class DbSingleton {
    private LiteOrm liteOrm;
    private static DbSingleton ourInstance = new DbSingleton();

    public static DbSingleton getInstance() {
        return ourInstance;
    }

    private DbSingleton() {
        liteOrm = LiteOrm.newCascadeInstance(MirrorApp.context, "mirror.db");

    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }
}
