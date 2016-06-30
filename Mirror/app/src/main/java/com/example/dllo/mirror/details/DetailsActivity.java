package com.example.dllo.mirror.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.buy.activity.BuyDetailsActivity;
import com.example.dllo.mirror.databean.AllBean;
import com.example.dllo.mirror.login.LoginActivity;
import com.example.dllo.mirror.net.MyImageListener;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.example.dllo.mirror.net.URLValues;
import com.example.dllo.mirror.wearpicture.WearPictureActivity;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dllo on 16/6/22.
 */
public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private NetTools netTools;
    private AutoRelativeLayout layout;
    private ListView inListView, outListView;//两层的listView
    private InAdapter inAdapter;
    private OutAdapter outAdapter;
    //给里层ListView头布局,绑定组件
    private ImageView shareImg;//分享
    private TextView goodsName;
    private TextView brand;
    private TextView infoDes;
    private TextView goodsPrice;
    private TextView brandTitle;
    //设置下面layout自动滑动的效果
    private int pos = -1;
    private boolean flag = true;
    private RelativeLayout relativeLayout;
    //下面的三个按钮
    private ImageView backImg;//返回
    private Button pdbtn;//佩戴图集
    private TextView buyTv;//购买
    private int position;//从AllBrowseFragment过来的
    //判断是不是登录状态
    private int value = 1;
    private AllBean allBean;

    @Override
    public int mSetContentView() {
        return R.layout.activity_details;
    }

    @Override
    public void initView() {
        relativeLayout = findView(R.id.activity_details_layout);
        inListView = findView(R.id.activity_details_in_list_view);
        outListView = findView(R.id.activity_details_out_list_view);
        inAdapter = new InAdapter(this);
        outAdapter = new OutAdapter(this);
        inListView.setAdapter(inAdapter);
        outListView.setAdapter(outAdapter);
        layout = findView(R.id.activity_details);
        netTools = new NetTools();
        //三个按钮
        backImg = findView(R.id.activity_details_back_img);
        pdbtn = findView(R.id.activity_details_pd_btn);
        buyTv = findView(R.id.all_categories_detail_buy_tv);
        backImg.setOnClickListener(this);
        pdbtn.setOnClickListener(this);
        buyTv.setOnClickListener(this);


        //给里层ListView加头布局,并绑定组件
        View inListHeadView = LayoutInflater.from(this).inflate(R.layout.details_in_list_head_view, null);
        inListView.addHeaderView(inListHeadView);
        shareImg = (ImageView) inListHeadView.findViewById(R.id.details_in_list_head_view_share);
        goodsName = (TextView) inListHeadView.findViewById(R.id.details_in_list_head_view_goods_name);
        brand = (TextView) inListHeadView.findViewById(R.id.details_in_list_head_view_brand);
        infoDes = (TextView) inListHeadView.findViewById(R.id.details_in_list_head_view_info_des);
        goodsPrice = (TextView) inListHeadView.findViewById(R.id.details_in_list_head_view_goods_price);
        brandTitle = (TextView) inListHeadView.findViewById(R.id.details_in_list_head_view_brand_title);
        shareImg.setOnClickListener(this);
        //给外层ListView加头布局
        View outListHeadView = LayoutInflater.from(this).inflate(R.layout.details_out_list_head_view, null);
        outListView.addHeaderView(outListHeadView);
        //给外层ListView加尾部布局
        View outListFlowView = LayoutInflater.from(this).inflate(R.layout.details_out_list_flow_view, null);
        outListView.addFooterView(outListFlowView);


    }

    @Override
    public void initData() {
        //设置背景图片(从AllBrowseFragment过来的)
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        String imgUrl = intent.getStringExtra("imgUrl");
        netTools.getBitmap(imgUrl, new MyImageListener() {
            @Override
            public void onGetBitmap(Bitmap bitmap) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                layout.setBackground(bitmapDrawable);
            }
        });
        //解析数据
        //请求体
        String body = "last_time=&device_type=3&page=&token=&version=1.0.1";
        //请求头
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/x-www-form-urlencoded");
        head.put("Content-Length", "51");
        head.put("Host", "api.mirroreye.cn");
        head.put("Connection", "Keep-Alive");
        head.put("Cookie", "PHPSESSID=0q0374jcg8m9so617bu65b1qm3; cp_language=zhUser-Agent");
        netTools.getData(URLValues.ALL_URL, new NetListener() {
            @Override
            public void onSuccessed(String string) {
                Gson gson = new Gson();
                allBean = gson.fromJson(string, AllBean.class);//Type1的类
                //给外层list设置数据
                List<AllBean.DataBean.ListBean.DataInfoBean.GoodsDataBean> goodsDataBeens =
                        allBean.getData().getList().get(position).getData_info().getGoods_data();
                outAdapter.setGoodsDataBeens(goodsDataBeens);
                //给里层list设置数据
                List<AllBean.DataBean.ListBean.DataInfoBean.DesignDesBean> designDesBeens =
                        allBean.getData().getList().get(position).getData_info().getDesign_des();
                inAdapter.setDesignDesBeens(designDesBeens);
                //给里层inListView加数据
                goodsName.setText(allBean.getData().getList().get(position).getData_info().getGoods_name());
                brand.setText(allBean.getData().getList().get(position).getData_info().getBrand());
                infoDes.setText(allBean.getData().getList().get(position).getData_info().getInfo_des());
                goodsPrice.setText(allBean.getData().getList().get(position).getData_info().getGoods_price());
                brandTitle.setText(allBean.getData().getList().get(position).getData_info().getBrand());
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        }, head, body);

        //里层listView滑动监听
        inListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //参考view里面的ListView的第一个Item
                View view1 = inListView.getChildAt(1);
                if (view1 == null) {
                    return;
                }
                //算法计算相对距离
                int scrolly = -view1.getTop() + inListView.getPaddingTop() +
                        inListView.getFirstVisiblePosition() * view1.getHeight();
                //设置外层listview的滑动距离
                outListView.setSelectionFromTop(1, -(int) (scrolly * 1.2));

                //增加过滤判断(动画效果)
                if (firstVisibleItem != pos) {
                    pos = firstVisibleItem;
                    //判断设置下面布局  可见与不可见(这里头布局占一个位置,也就是屏幕上出现List的Item为1的时候出现)
                    if (firstVisibleItem == 1 && flag) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        setAnimation();
                        flag = false;
                    } else if (firstVisibleItem == 0 && !flag) {
                        setDismissAnimation();
                        flag = true;
                    }
                }
            }
        });

        //给里层的listview设置焦点
        outListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return inListView.dispatchTouchEvent(event);
            }
        });


    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_in_list_head_view_share://分享
                break;
            case R.id.activity_details_back_img://退出
                finish();
                break;
            case R.id.activity_details_pd_btn://佩戴图集
                Intent intent = new Intent(this, WearPictureActivity.class);
                intent.putExtra("pos", position);
                startActivity(intent);
                break;
            case R.id.all_categories_detail_buy_tv://购买
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
                break;

        }
    }

    //设置下面字体平移动画效果(让布局从左面滑倒屏幕上)
    public void setAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1,//x起始点
                Animation.RELATIVE_TO_PARENT, 0,//y起始点
                Animation.RELATIVE_TO_PARENT, 0,//x终点
                Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setRepeatCount(0);
        relativeLayout.startAnimation(translateAnimation);
    }

    //让布局从屏幕滑倒左面(就是滑倒左面消失)
    public void setDismissAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setRepeatCount(0);
        relativeLayout.startAnimation(translateAnimation);
        //给动画设置监听,GONE掉
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //设置布局消失
                relativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
