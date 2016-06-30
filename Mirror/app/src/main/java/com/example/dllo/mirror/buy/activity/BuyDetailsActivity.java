package com.example.dllo.mirror.buy.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by dllo on 16/6/28.
 */
public class BuyDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView goodsPicImg;
    private TextView goodsPriceTv;
    private TextView goodsNameTv;
    private TextView addManTv;//添加联系人信息
    private TextView manTv;//收件人
    private TextView manNameTv;//收件人名字
    private TextView addressTv;//地址
    private TextView manAddressTv;//收件人地址
    private TextView phoneTv;//电话
    private TextView manPhoneTv;//收件人电话
    private ImageView closeImg;//关闭
    private TextView changeAddressTv;//更改地址
    private TextView addAddressTv;//添加地址

    @Override

    public int mSetContentView() {
        return R.layout.activity_buy_details;
    }

    @Override
    public void initView() {
        goodsPicImg = findView(R.id.activity_buy_details_price_img);
        goodsPriceTv = findView(R.id.activity_buy_details_goods_price);
        goodsNameTv = findView(R.id.activity_buy_details_price_goods_name);

        closeImg = findView(R.id.activity_buy_details_close);
        addManTv = findView(R.id.activity_buy_details_add_man);//添加联系人信息
        manTv = findView(R.id.activity_buy_details_man);//收件人
        manNameTv = findView(R.id.activity_buy_details_man_tv);//收件人名字
        addressTv = findView(R.id.activity_buy_details_address);//地址
        manAddressTv = findView(R.id.activity_buy_details_address_tv);//收件人地址
        phoneTv = findView(R.id.activity_buy_details_phone);//电话
        manPhoneTv = findView(R.id.activity_buy_details_phone_tv);//收件人电话
        changeAddressTv = findView(R.id.activity_buy_details_change_address);//更改地址
        addAddressTv = findView(R.id.activity_buy_details_add_address);//添加地址


        closeImg.setOnClickListener(this);
        addAddressTv.setOnClickListener(this);

    }

    @Override
    public void initData() {
        //DetailsActivity跳过来的
        Intent intent = getIntent();
        String goodsPic = intent.getStringExtra("goodsPic");//图片
        String goodsPrice = intent.getStringExtra("goodsPrice");//价钱
        String goodsName = intent.getStringExtra("goodsName");//名字
        Picasso.with(this).load(goodsPic).fit().into(goodsPicImg);
        goodsPriceTv.setText(goodsPrice);
        goodsNameTv.setText(goodsName);

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_buy_details_close://关闭
                finish();
                break;
            case R.id.activity_buy_details_add_address://添加地址
                Intent intent = new Intent(this,MyAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
