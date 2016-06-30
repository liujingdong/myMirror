package com.example.dllo.mirror.net;

import com.android.volley.VolleyError;

/**
 * Created by dllo on 16/6/20.
 */
public interface NetListener {
    void onSuccessed(String string);
    void onFailed(VolleyError error);
}
