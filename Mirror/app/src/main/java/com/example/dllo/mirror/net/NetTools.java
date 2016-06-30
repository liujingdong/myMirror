package com.example.dllo.mirror.net;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.mian.MirrorApp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dllo on 16/6/20.
 */

//所有网络的请求工具(POST请求)
public class NetTools {
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public NetTools() {
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        imageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    /**
     * @param url         出入的网址
     * @param netListener 接口
     * @param head        请求头
     * @param body        请求体
     */

    //请求体是一个String
    public void getData(final String url, final NetListener netListener, final Map head, final String body) {
        if (isNetworkAvailable(MirrorApp.context)) {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    netListener.onSuccessed(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    netListener.onFailed(error);
                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return head;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.getBytes();
                }
            };
            requestQueue.add(request);
        } else {
            CountDownTimer timer = new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    String allBean = new String(requestQueue.getCache().get(url).data);
                    netListener.onSuccessed(allBean);
                }
            }.start();
        }
    }

    //请求体是键值对的方法
    public void getDatas(String url, final NetListener netListener, final Map body) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                netListener.onSuccessed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netListener.onFailed(error);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }
        };
        requestQueue.add(request);
    }

    //请求图片ImageLoader(我还是喜欢毕加索)

    /**
     * @param imgUrl 传进来的图片网址
     * @param img    传进来的img
     */
    public void getImage(String imgUrl, ImageView img, Boolean flag) {
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        imageLoader = new ImageLoader(requestQueue, new MemoryCache());//MemoryCache()缓存的类,自己写的

        if (flag) {
            //加动画效果啦
            imageLoader.get(imgUrl, new ImageAlpha(img, R.mipmap.loading, R.mipmap.ic_launcher));
        } else {
            //没有加动画效果(留一个后手吧,宝宝心里苦,可是宝宝不说)
            imageLoader.get(imgUrl, ImageLoader.getImageListener(img, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        }
    }

    //解析后返回Bitmap的方法
    public void getBitmap(String imgUrl, final MyImageListener listener) {
        imageLoader.get(imgUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() == null) {
                    Bitmap b = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.loading);
                    listener.onGetBitmap(b);
                } else {
                    listener.onGetBitmap(response.getBitmap());
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    //写一个自定义的动画玩玩(只有透明度的显示哈,其他没有加)
    class ImageAlpha implements ImageLoader.ImageListener {
        ImageView img;
        int defaultImg, errorImg;//一个是默认的图片,一个是解析错误的图片

        public ImageAlpha(ImageView img, int defaultImg, int errorImg) {
            this.img = img;
            this.defaultImg = defaultImg;
            this.errorImg = errorImg;
        }

        //计算缩放比例
        public float getScaleNum(ImageView imageView, Bitmap bitmap) {
            float num = 0f;
            float scaleH, scaleW;
            //先算宽
            WindowManager windowManager = (WindowManager) MirrorApp.context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);

            if (imageView.getLayoutParams().width > 0) {
                scaleW = imageView.getLayoutParams().width / (float) bitmap.getWidth();
            } else {
                scaleW = displayMetrics.widthPixels / (float) bitmap.getWidth();
            }

            //高
            if (imageView.getLayoutParams().height > 0) {
                scaleH = imageView.getLayoutParams().height / (float) bitmap.getHeight();
            } else {
                scaleH = displayMetrics.heightPixels / (float) bitmap.getHeight();
            }

            num = Math.min(scaleH, scaleW);


            return num > 1 ? 1 : num;
        }

        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            if (response.getBitmap() != null) {
                //把网上的高清图片缩小,减少内存占用
                Matrix matrix = new Matrix();
                Bitmap bitmapFrom = response.getBitmap();//拿到Bitmap
                //缩放的比例 是应该要计算
                float scaleNum = getScaleNum(img, bitmapFrom);
                if (scaleNum != 1) {
                    matrix.postScale(scaleNum, scaleNum);//按比例缩小
                    Bitmap bitmap = Bitmap.createBitmap(
                            bitmapFrom, 0, 0, bitmapFrom.getWidth(), bitmapFrom.getHeight(), matrix, false);
                    bitmapFrom.recycle();//释放掉资源

                    img.setImageBitmap(bitmap);

                } else {
                    img.setImageBitmap(bitmapFrom);
                }

//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img, "alpha", 0, 1);
//                objectAnimator.setDuration(300);//设置持续时间为0.3秒
//                objectAnimator.start();
            } else if (defaultImg != 0) {
                img.setImageResource(defaultImg);
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }

    //判断网络
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
