package com.example.dllo.mirror.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.mian.MainActivity;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.example.dllo.mirror.net.URLValues;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dllo on 16/6/22.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView closeImg;
    private EditText phoneEt;//手机号
    private EditText passwordEt;//密码
    private Button loginBtn;//登录
    private Button createBtn;//创建账号
    private NetTools netTools;


    @Override
    public int mSetContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        netTools = new NetTools();
        closeImg = findView(R.id.activity_login_close);
        phoneEt = findView(R.id.activity_login_phone_et);
        passwordEt = findView(R.id.activity_login_password_et);
        loginBtn = findView(R.id.activity_login_login_btn);
        createBtn = findView(R.id.activity_login_create_account_btn);
        closeImg.setOnClickListener(this);
        phoneEt.setOnClickListener(this);
        passwordEt.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);

    }

    @Override
    public void initData() {
        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (phoneEt.getText().length() == 11 && passwordEt.getText().length() != 0) {
                        loginBtn.setBackground(getDrawable(R.mipmap.create_account));
                    } else {
                        loginBtn.setBackground(getDrawable(R.mipmap.no_use));
                    }
                }
            }
        });

        //输入手机号的EditText的输入监听
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && passwordEt.getText().length() != 0) {
                    loginBtn.setBackground(getDrawable(R.mipmap.create_account));
                } else {
                    loginBtn.setBackground(getDrawable(R.mipmap.no_use));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //密码EditText的输入监听
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (phoneEt.getText().length() == 11 && s.length() != 0) {
                    loginBtn.setBackground(getDrawable(R.mipmap.create_account));
                } else {
                    loginBtn.setBackground(getDrawable(R.mipmap.no_use));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_close://关闭
                finish();
                break;
            case R.id.activity_login_phone_et://手机号
                break;
            case R.id.activity_login_password_et://密码
                break;
            case R.id.activity_login_login_btn://登录
                Map<String, String> body = new HashMap<>();
                body.put("phone_number", phoneEt.getText().toString());
                body.put("password", passwordEt.getText().toString());
                netTools.getDatas(URLValues.LOGIN_URL, new NetListener() {
                    @Override
                    public void onSuccessed(String string) {
                        Gson gson = new Gson();
                        LoginBean loginBean = gson.fromJson(string,LoginBean.class);
                        switch (loginBean.getResult()){
                            case "1":
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                //发一个广播,通知首页把"登录"改成"购物车"
                                Intent intentBroad = new Intent("com.example.dllo.mirror.CAR");
                                sendBroadcast(intentBroad);


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                //弄一个SP用来判断详情页面点击购买是跳转哪个界面
                                SharedPreferences sp = getSharedPreferences("ifLogin",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("loginBoolean",true);//登录啦就给一个true
                                editor.putString("token",loginBean.getData().getToken());
                                editor.commit();
                                break;
                            case "0":
                                Toast.makeText(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailed(VolleyError error) {

                    }
                }, body);
                break;
            case R.id.activity_login_create_account_btn://创建账号
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
