package com.example.dllo.mirror.tools;

import android.util.Log;

/**
 * Created by dllo on 16/6/20.
 */
public class LogM {
    private static boolean flag = true;

    public static void logM(String tag, String msg) {
        if (flag) {
            Log.i(tag, msg);
        }

    }
}
