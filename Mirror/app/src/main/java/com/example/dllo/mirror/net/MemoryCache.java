package com.example.dllo.mirror.net;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dllo on 16/6/20.
 * 图片缓存
 */
public class MemoryCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> lruCache;//最近最少使用
    private ConcurrentHashMap<String, SoftReference<Bitmap>> hashMap;

    public MemoryCache() {
        //获取最大内存的8分之1
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 10 / 1024);
        hashMap = new ConcurrentHashMap<>();
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
            //二级缓存
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                SoftReference<Bitmap> bitmapSoftReference = new SoftReference<Bitmap>(oldValue);
                hashMap.put(key, bitmapSoftReference);
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        if (lruCache.get(url) == null) {

            if (hashMap.get(url) == null) {
                hashMap.remove(url);
                return null;
            } else {
                return hashMap.get(url).get();
            }
        }
        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

        lruCache.put(url, bitmap);

    }
}
