package com.example.dllo.mirror.buy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.buy.adapter.MyAddressAdapter;
import com.example.dllo.mirror.buy.bean.MyAddressBean;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dllo on 16/6/29.
 */
public class MyAddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView closeImg;
    private Button myAddressTv;
    private ListView listView;
    private MyAddressAdapter myAddressAdapter;
    private NetTools netTools;

    @Override
    public int mSetContentView() {
        return R.layout.activity_my_address;
    }

    @Override
    public void initView() {
        netTools = new NetTools();
        closeImg = findView(R.id.activity_my_myress_close);
        myAddressTv = findView(R.id.activity_my_address_add_address_btn);
        listView = findView(R.id.activity_my_address_list_view);
        myAddressAdapter = new MyAddressAdapter(this);
        listView.setAdapter(myAddressAdapter);

        closeImg.setOnClickListener(this);
        myAddressTv.setOnClickListener(this);

    }

    @Override
    public void initData() {
        //listView的点击事件
        //短点击添加默认地址
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //长点击删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });

    }

    //让它每次都走一遍,达到刷新的效果
    @Override
    protected void onResume() {
        super.onResume();
        String url = "http://api.mirroreye.cn/index.php/user/address_list";
        final SharedPreferences getSp = getSharedPreferences("ifLogin", MODE_PRIVATE);
        String token = getSp.getString("token", null);
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("device_type", "1");
        netTools.getDatas(url, new NetListener() {
            @Override
            public void onSuccessed(String string) {
                Gson gson = new Gson();
                MyAddressBean myAddressBean = gson.fromJson(string, MyAddressBean.class);
                myAddressAdapter.setMyAddressBean(myAddressBean);
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(MyAddressActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
            }
        }, body);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_my_myress_close://关闭
                finish();
                break;
            case R.id.activity_my_address_add_address_btn://添加地址
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
