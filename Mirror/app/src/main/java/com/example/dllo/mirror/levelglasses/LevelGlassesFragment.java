package com.example.dllo.mirror.levelglasses;

import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseFragment;
import com.example.dllo.mirror.mian.MainActivity;
import com.example.dllo.mirror.popupwindow.PopupWindowBean;
import com.example.dllo.mirror.popupwindow.PopupWindowView;

/**
 * Created by dllo on 16/6/21.
 */
public class LevelGlassesFragment extends BaseFragment implements View.OnClickListener {
    private TextView levelGlassesTv;

    @Override
    public int setLayout() {
        return R.layout.fragment_level_glasses;
    }

    @Override
    public void initView(View view) {
        levelGlassesTv = findView(R.id.fragment_level_glasses_tv);
        levelGlassesTv.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_level_glasses_tv:
                //获取通知栏的高度
                Rect frame = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                PopupWindowView popupWindowView = new PopupWindowView
                        (context,((MainActivity)getActivity()).getHeight()+statusBarHeight);

                //把值传过去比较
                PopupWindowBean popupWindowBean = new PopupWindowBean();
                popupWindowBean.setIndex(1);
                popupWindowView.setBusBean(popupWindowBean);
                popupWindowView.initPopupWindow();
                break;
        }
    }
}
