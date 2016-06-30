package com.example.dllo.mirror.net;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.dllo.mirror.mian.MirrorApp;

/**
 * Created by dllo on 16/6/20.
 */
public class VolleySingleton {
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    private static VolleySingleton ourInstance = new VolleySingleton();

    public static VolleySingleton getInstance() {
        return ourInstance;
    }

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(MirrorApp.context);
        imageLoader = new ImageLoader(requestQueue, new MemoryCache());
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
