package com.example.dllo.mirror.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.mian.ChangViewPagerBean;
import com.example.dllo.mirror.mian.MainActivity;
import com.example.dllo.mirror.mian.MirrorApp;


/**
 * Created by dllo on 16/6/23.
 */
public class PopupWindowView implements View.OnClickListener {
    private PopupWindow popupWindow;
    private Context context;
    private TextView allBrowseTv;//所有浏览
    private TextView levelGlassesTv;//平光镜
    private TextView sunGlassesTv;//太阳镜
    private TextView specialShareTv;//特别分享
    private TextView shoppingCarTv;//我的购物车
    private TextView backHomeTv;//返回首页
    private TextView exitTv;//退出
    private ImageView allBrowseLine, levelGlassesLine, sunGlassesLine, specialShareLine, shoppingCarLine, backHomeLine, exitLine;
    private ImageView backGroundImg;
    private int loginHeight;
    private PopupWindowBean busBean;
    private ChangViewPagerBean changViewPagerBean;

    private TextView[] textViews = {allBrowseTv, levelGlassesTv, sunGlassesTv, specialShareTv
            , shoppingCarTv, backHomeTv, exitTv};

    private int[] textViewsIds = {R.id.popup_view_all_browse_tv, R.id.popup_view_level_glasses_tv,
            R.id.popup_view_sun_glasses_tv, R.id.popup_view_special_share_tv,
            R.id.popup_view_shopping_car_tv, R.id.popup_view_back_home_tv, R.id.popup_view_exit_tv};


    private ImageView[] imageViews = {allBrowseLine, levelGlassesLine, sunGlassesLine
            , specialShareLine, shoppingCarLine, backHomeLine, exitLine};
    private int[] imageViewIds = {R.id.popup_view_all_browse_line,
            R.id.popup_view_level_glasses_line,
            R.id.popup_view_sun_glasses_line,
            R.id.popup_view_special_share_line,
            R.id.popup_view_shopping_car_line,
            R.id.popup_view_back_home_line,
            R.id.popup_view_exit_line};
    private AlertDialog dialog;//命令提示框(自定义的那个,现在不用啦啊)

    //获取MainActivity里面"登录"+"通知栏"的高度
    public PopupWindowView(Context context, int height) {
        this.context = context;
        this.loginHeight = height;
        changViewPagerBean = new ChangViewPagerBean();
    }

    public void setBusBean(PopupWindowBean busBean) {
        this.busBean = busBean;
    }

    public void initPopupWindow() {
        //获取手机屏幕的高度
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        //设置满屏状态(高度减去"登录"+"通知栏"的高度,把下边的字露出来)
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,
                height - loginHeight);
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_view, null);
        backGroundImg = (ImageView) popupView.findViewById(R.id.popup_view_back_ground_img);
        backGroundImg.setOnClickListener(this);

        //绑定布局
        for (int i = 0; i < textViews.length; i++) {
            textViews[i] = (TextView) popupView.findViewById(textViewsIds[i]);
        }

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = (ImageView) popupView.findViewById(imageViewIds[i]);
        }

        //点击事件
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setOnClickListener(this);
        }

        popupWindow.setContentView(popupView);
        popupWindow.showAsDropDown(popupView);
        //判断哪个字亮
        switch (busBean.getIndex()) {
            case 0://所有
                setTextView(0);
                break;
            case 1://平光眼镜
                setTextView(1);
                break;
            case 2://太阳镜
                setTextView(2);
                break;
            case 3://专享
                setTextView(3);
                break;
            case 4://购物车
                setTextView(4);
                break;
        }
    }

    public void setTextView(int a) {
        for (int i = 0; i < textViews.length; i++) {
            if (i == a) {
                textViews[i].setTextColor(Color.WHITE);
                imageViews[i].setVisibility(View.VISIBLE);
            } else {
                textViews[i].setTextColor(Color.WHITE);
                textViews[i].setAlpha(0.25f);
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_view_back_ground_img://点击其他地方退出popupWindow
                popupWindow.dismiss();
                break;
            case R.id.popup_view_all_browse_tv://所有浏览0
                changViewPager(0);
                break;
            case R.id.popup_view_level_glasses_tv://浏览平光眼镜1
                changViewPager(1);
                break;
            case R.id.popup_view_sun_glasses_tv://浏览太阳眼镜2
                changViewPager(2);
                break;
            case R.id.popup_view_special_share_tv://专题分享3
                changViewPager(3);
                break;
            case R.id.popup_view_shopping_car_tv://我的购物车4
                changViewPager(4);
                break;
            case R.id.popup_view_back_home_tv://返回首页0
                changViewPager(0);
                break;
            case R.id.popup_view_exit_tv://退出
                popupWindow.dismiss();
                SharedPreferences getSp = context.getSharedPreferences("ifLogin", context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = getSp.edit();
                boolean flag = getSp.getBoolean("loginBoolean", false);
                if (flag) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                    View view = LayoutInflater.from(context).inflate(R.layout.popup_alert, null);
//                    Button noBtn = (Button) view.findViewById(R.id.popup_alter_no_btn);
//                    Button yesBtn = (Button) view.findViewById(R.id.popup_alter_yes_btn);
//                    noBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();//自定义让他消失
//                        }
//                    });
//                    yesBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //发一个广播,通知首页把"登录"改成"购物车"
//                            Intent intentBroad = new Intent("com.example.dllo.mirror.LOGIN");
//                            context.sendBroadcast(intentBroad);
//
//                        }
//                    });
//                    alert.setView(view);
//                    dialog = alert.show();//(自定义的那个)

                    alert.setTitle("确定退出登录");
                    alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //发一个广播,通知首页把"登录"改成"购物车"
                            Intent intentBroad = new Intent("com.example.dllo.mirror.LOGIN");
                            context.sendBroadcast(intentBroad);
                            editor.clear();
                            editor.commit();
                        }
                    });
                    alert.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.show();
                } else {
                    Toast.makeText(context, "你还没有登录,请登录", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    //发一个广播改变viewPager的方法.发到MainActivity里面啦
    public void changViewPager(int data) {
        Intent intent = new Intent("com.example.dllo.mirror.CHANG_VIEW_PAGER");
        intent.putExtra("changData", data);
        context.sendBroadcast(intent);
        popupWindow.dismiss();
    }
}
