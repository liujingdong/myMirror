package com.example.dllo.mirror.mian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.allbrowse.AllBrowseFragment;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.customview.VerticalViewPager;
import com.example.dllo.mirror.levelglasses.LevelGlassesFragment;
import com.example.dllo.mirror.login.LoginActivity;
import com.example.dllo.mirror.shoppingcar.ShoppingCarFragment;
import com.example.dllo.mirror.specialshare.SpecialShareFragment;
import com.example.dllo.mirror.sunglasses.SunGlassesFragment;
import com.example.dllo.mirror.tools.LogM;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Fragment> fragments;
    private FragmentAdapter fragmentAdapter;
    private VerticalViewPager viewPager;
    private TextView loginTv;//登录
    private ImageView mirrorImg;//美若图片
    private CarBroadcastReceiver carBroadcastReceiver;//登录的广播接受者
    private LoginBroadcastReceiver loginBroadcastReceiver;//创建账号的
    private AllBrowseFragment allBrowseFragment;
    private LevelGlassesFragment levelGlassesFragment;
    private SunGlassesFragment sunGlassesFragment;
    private SpecialShareFragment specialShareFragment;
    private ShoppingCarFragment shoppingCarFragment;
    private ChangViewPagerBroadcastReceiver changViewPagerBroadcastReceiver;

    //获取登录Tv的高度
    public int getHeight(){
        return loginTv.getHeight();
    }

    @Override
    public int mSetContentView() {
        allBrowseFragment = new AllBrowseFragment();
        levelGlassesFragment = new LevelGlassesFragment();
        sunGlassesFragment = new SunGlassesFragment();
        specialShareFragment = new SpecialShareFragment();
        shoppingCarFragment = new ShoppingCarFragment();
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        viewPager = findView(R.id.main_vertical_view_pager);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragments = new ArrayList<>();
        loginTv = findView(R.id.activity_main_login);
        mirrorImg = findView(R.id.main_vertical_mirror_view);
        loginTv.setOnClickListener(this);
        mirrorImg.setOnClickListener(this);

    }

    @Override
    public void initData() {
        viewPager.internalCanScrollVertically(View.FOCUS_LEFT);
        fragments.add(allBrowseFragment);//全部浏览
        fragments.add(levelGlassesFragment);//浏览平光镜
        fragments.add(sunGlassesFragment);//浏览太阳镜
        fragments.add(specialShareFragment);//专题分享
        fragments.add(shoppingCarFragment);//我的购物车
        fragmentAdapter.setFragments(fragments);
        viewPager.setAdapter(fragmentAdapter);

        //登录和创建账号的广播注册(记得取消注册哦宝宝们,静态的不用啦,可是这个是动态的啊 )
        carBroadcastReceiver = new CarBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.dllo.mirror.CAR");
        registerReceiver(carBroadcastReceiver,intentFilter);
        //退出登录时候的
        loginBroadcastReceiver = new LoginBroadcastReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.example.dllo.mirror.LOGIN");
        registerReceiver(loginBroadcastReceiver,intentFilter1);
        //改变ViewPager页面的广播注册
        changViewPagerBroadcastReceiver = new ChangViewPagerBroadcastReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.example.dllo.mirror.CHANG_VIEW_PAGER");
        registerReceiver(changViewPagerBroadcastReceiver,intentFilter2);

        //判断是不是登录的状态
        SharedPreferences getSp = getSharedPreferences("ifLogin", MODE_PRIVATE);
        boolean flag = getSp.getBoolean("loginBoolean", false);
        if(flag){
            loginTv.setText("购物车");
        }else {
            loginTv.setText("登录");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_login://同一个id不同的点击事件("登录"和"购物车")
                switch (loginTv.getText().toString()){
                    case "登录":
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "购物车":
                        viewPager.setCurrentItem(4);
                        break;
                }
                break;
            case R.id.main_vertical_mirror_view:
                showScaleAnim(mirrorImg);
                break;
        }
    }

    //登录和注册后的广播接受者(就是把登录改成购物车)
    class CarBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            loginTv.setText("购物车");
        }
    }

    //退出登录后的广播接受者(把登录的"购物车"改成"登录")
    class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            loginTv.setText("登录");
        }
    }

    /*
    SharedPreferences getSp = getSharedPreferences("ifLogin", MODE_PRIVATE);
                boolean flag = getSp.getBoolean("loginBoolean", false);
                if (flag) {//跳到购买
                    //图片往事
                    String goodsPic = allBean.getData().getList().get(position).getData_info().getGoods_pic();
                    //价钱
                    String goodsPrice = allBean.getData().getList().get(position).getData_info().getGoods_price();
                    //英文名字
                    String goodsName = allBean.getData().getList().get(position).getData_info().getGoods_name();
                    Intent intentBuy = new Intent(this, BuyDetailsActivity.class);
                    intentBuy.putExtra("goodsPic", goodsPic);
                    intentBuy.putExtra("goodsPrice", goodsPrice);
                    intentBuy.putExtra("goodsName", goodsName);
                    startActivity(intentBuy);
                } else {//跳到登录
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentLogin);
                }
     */





    //改变ViewPager页面的广播
    class ChangViewPagerBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int changData = intent.getIntExtra("changData",0);
            viewPager.setCurrentItem(changData);
        }
    }

    //设置mirror动画
    public void showScaleAnim(ImageView imageView){
        //前两个参数是x轴从多少到多少
        //3、4参数是y轴从多少到多少
        //5.6参数是缩放点
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,1.1f,1f,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setRepeatCount(1);//1为动两次
        scaleAnimation.setDuration(300);//持续时间
        imageView.startAnimation(scaleAnimation);//开始动画
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(carBroadcastReceiver);
        unregisterReceiver(loginBroadcastReceiver);
        unregisterReceiver(changViewPagerBroadcastReceiver);
    }

}
