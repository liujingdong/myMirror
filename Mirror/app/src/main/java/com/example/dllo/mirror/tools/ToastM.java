package com.example.dllo.mirror.tools;

import android.widget.Toast;

import com.example.dllo.mirror.mian.MirrorApp;

/**
 * Created by dllo on 16/6/20.
 */
public class ToastM {
    private static boolean flag = true;

    public static void toastM(String str) {
        if (flag) {
            Toast.makeText(MirrorApp.context, str, Toast.LENGTH_SHORT).show();
        }
    }

}
