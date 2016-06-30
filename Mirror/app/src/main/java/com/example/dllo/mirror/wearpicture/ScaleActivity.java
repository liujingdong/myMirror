package com.example.dllo.mirror.wearpicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.customview.SmoothImageView;
import com.example.dllo.mirror.net.NetTools;

/**
 * Created by dllo on 16/6/28.
 */
public class ScaleActivity extends AppCompatActivity implements View.OnClickListener {
    private NetTools netTools;
//    private ImageView img;
    private String imgUrl;


    private SmoothImageView imageView;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
//        img = (ImageView) findViewById(R.id.activity_scale);

//        img.setOnClickListener(this);
        netTools = new NetTools();
        //拿到WearPictureActivity的数据
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOnClickListener(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();//进

        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        netTools.getImage(imgUrl, imageView, true);
    }


    //点击事件
    @Override
    public void onClick(View v) {
//        showScaleAnim();
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformOut();//退
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                finish();
            }
        });
    }

    //动画效果
//    public void showScaleAnim() {
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.6f, 1f, 0.6f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setRepeatCount(0);
//        scaleAnimation.setDuration(1000);
//        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                finish();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        img.startAnimation(scaleAnimation);
//    }
}
