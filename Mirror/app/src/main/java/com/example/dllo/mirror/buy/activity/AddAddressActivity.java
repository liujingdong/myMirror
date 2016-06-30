package com.example.dllo.mirror.buy.activity;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.buy.bean.AddAddressBean;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dllo on 16/6/29.
 */
public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView closeImg;
    private EditText nameEt, phoneEt, addressEt;
    private Button commitBtn;//提交地址
    private NetTools netTools;

    @Override
    public int mSetContentView() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initView() {
        netTools = new NetTools();
        closeImg = findView(R.id.activity_add_address_close);
        nameEt = findView(R.id.activity_add_address_name_et);
        phoneEt = findView(R.id.activity_add_address_phone_et);
        addressEt = findView(R.id.activity_add_address_address_et);
        commitBtn = findView(R.id.activity_add_address_commit_address_btn);
        closeImg.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_address_close://关闭
                finish();
                break;
            case R.id.activity_add_address_commit_address_btn://提交地址
                if (TextUtils.isEmpty(nameEt.getText()) && TextUtils.isEmpty(phoneEt.getText())
                        && TextUtils.isEmpty(addressEt.getText())) {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://api.mirroreye.cn/index.php/user/add_address";
                    final SharedPreferences getSp = getSharedPreferences("ifLogin", MODE_PRIVATE);
                    String token = getSp.getString("token", null);
                    Map<String, String> body = new HashMap<>();
                    body.put("token", token);
                    body.put("username", nameEt.getText().toString());
                    body.put("cellphone", phoneEt.getText().toString());
                    body.put("addr_info", addressEt.getText().toString());
                    netTools.getDatas(url, new NetListener() {
                        @Override
                        public void onSuccessed(String string) {
                            Gson gson = new Gson();
                            AddAddressBean addAddressBean = gson.fromJson(string, AddAddressBean.class);
                            switch (addAddressBean.getResult()) {
                                case "0":
                                    Toast.makeText(AddAddressActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case "1":
                                    Toast.makeText(AddAddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                            }
                        }

                        @Override
                        public void onFailed(VolleyError error) {
                            Toast.makeText(AddAddressActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }, body);
                }

                break;
        }
    }
}
