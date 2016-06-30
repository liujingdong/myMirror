package com.example.dllo.mirror.specialshare;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseFragment;
import com.example.dllo.mirror.mian.MainActivity;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.example.dllo.mirror.net.URLValues;
import com.example.dllo.mirror.popupwindow.PopupWindowBean;
import com.example.dllo.mirror.popupwindow.PopupWindowView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dllo on 16/6/21.
 */
public class SpecialShareFragment extends BaseFragment implements View.OnClickListener {
    private TextView specialShareTv;
    private NetTools netTools;
    private SpecialShareAdapter specialShareAdapter;
    private RecyclerView recyclerView;
    @Override
    public int setLayout() {
        return R.layout.fragment_special_share;
    }

    @Override
    public void initView(View view) {
        recyclerView = findView(R.id.fragment_special_share_recycler_view);
        specialShareTv = findView(R.id.fragment_special_share_tv);
        specialShareTv.setOnClickListener(this);
        netTools = new NetTools();
        specialShareAdapter = new SpecialShareAdapter(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(specialShareAdapter);

    }

    @Override
    public void initData() {
        Map<String,String> body = new HashMap<>();
        body.put("device_type","1");
        netTools.getDatas(URLValues.SHARE_URL, new NetListener() {
            @Override
            public void onSuccessed(String string) {
                Gson gson = new Gson();
                SpecialShareBean specialShareBean = gson.fromJson(string,SpecialShareBean.class);
                specialShareAdapter.setSpecialShareBean(specialShareBean);
            }

            @Override
            public void onFailed(VolleyError error) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
            }
        },body);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_special_share_tv:
                //获取通知栏的高度
                Rect frame = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                PopupWindowView popupWindowView = new PopupWindowView
                        (context,((MainActivity)getActivity()).getHeight()+statusBarHeight);

                //把值传过去比较
                PopupWindowBean popupWindowBean = new PopupWindowBean();
                popupWindowBean.setIndex(3);
                popupWindowView.setBusBean(popupWindowBean);
                popupWindowView.initPopupWindow();
                break;
        }
    }
}
