package com.example.dllo.mirror.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView closeImg;
    private TextView sendCodeEt;
    private EditText phoneEt;
    private NetTools netTools;
    private SendCodeBean sendCodeBean;
    private Button createAccountBtn;
    private EditText passwordEt;
    private Gson gson;

    @Override
    public int mSetContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        netTools = new NetTools();
        gson = new Gson();
        closeImg = findView(R.id.activity_register_close);
        sendCodeEt = findView(R.id.activity_register_send_code_btn);
        phoneEt = findView(R.id.activity_register_phone_et);//手机号
        createAccountBtn = findView(R.id.activity_register_create_account_btn);//创建账号
        passwordEt = findView(R.id.activity_register_password_et);
        createAccountBtn.setOnClickListener(this);
        closeImg.setOnClickListener(this);
        sendCodeEt.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_register_close://关闭
                finish();
                break;
            case R.id.activity_register_send_code_btn://发送短信 验证码
                final Map<String, String> body = new HashMap<>();
                body.put("phone number", phoneEt.getText().toString());
                netTools.getDatas(URLValues.SEND_CODE_URL, new NetListener() {
                    @Override
                    public void onSuccessed(String string) {
                        sendCodeBean = gson.fromJson(string,SendCodeBean.class);
                        switch (sendCodeBean.getResult()){
                            case "1":
                                Toast.makeText(RegisterActivity.this, "获取短信验证码成功", Toast.LENGTH_SHORT).show();
                                break;
                            case "0":
                                Toast.makeText(RegisterActivity.this, sendCodeBean.getMsg(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailed(VolleyError error) {

                    }
                }, body);
                break;
            case R.id.activity_register_create_account_btn://创建账号
                Map<String,String> accountBody = new HashMap<>();
                accountBody.put("phone_number",phoneEt.getText().toString());
                accountBody.put("number",sendCodeBean.getData());
                accountBody.put("password",passwordEt.getText().toString());
                netTools.getDatas(URLValues.SEND_CODE_URL, new NetListener() {
                    @Override
                    public void onSuccessed(String string) {
                        CreateAccountBean accountBean = gson.fromJson(string,CreateAccountBean.class);
                        switch (accountBean.getResult()){
                            case "1":
                                Toast.makeText(RegisterActivity.this, "创建账号成功", Toast.LENGTH_SHORT).show();
                                //发一个广播,通知首页把"登录"改成"购物车"
                                Intent intentBroad = new Intent("com.example.dllo.mirror.CAR");
                                sendBroadcast(intentBroad);

                                //用来判断是不是登录的状态
                                SharedPreferences sp = getSharedPreferences("ifLogin",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("loginBoolean",true);//登录啦就给一个true
                                editor.commit();


                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case "0":
                                Toast.makeText(RegisterActivity.this, accountBean.getMsg(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    @Override
                    public void onFailed(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                },accountBody);
                break;
        }
    }
}
