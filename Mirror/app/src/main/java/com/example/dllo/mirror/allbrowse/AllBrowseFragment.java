package com.example.dllo.mirror.allbrowse;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseFragment;
import com.example.dllo.mirror.details.DetailsActivity;
import com.example.dllo.mirror.details.Type2DetailsActivity;
import com.example.dllo.mirror.mian.MainActivity;
import com.example.dllo.mirror.mian.MirrorApp;
import com.example.dllo.mirror.popupwindow.PopupWindowBean;
import com.example.dllo.mirror.popupwindow.PopupWindowView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dllo on 16/6/21.
 */
public class AllBrowseFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private AllBrowseAdapter allBrowseAdapter;
    private TextView allBrowseTv;

    @Override
    public int setLayout() {
        return R.layout.fragment_all_browse;
    }

    @Override
    public void initView(View view) {
        recyclerView = findView(R.id.fragment_all_browse_recycler_view);
        allBrowseTv = findView(R.id.fragment_all_browse);
        allBrowseTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

        allBrowseAdapter = new AllBrowseAdapter(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(allBrowseAdapter);

        //点击跳转
        allBrowseAdapter.setOnClickListener(new AllBrowseOnClickListener() {
            @Override
            public void onClick(String imgUrl,int type,int position) {
                if(type == 1){
                    //传到DetailsActivity
                    Intent intent = new Intent(MirrorApp.context, DetailsActivity.class);
                    intent.putExtra("imgUrl", imgUrl);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }else {
                    //Type传的值不对(还没有写到)
                    Intent intent = new Intent(MirrorApp.context, Type2DetailsActivity.class);
                    intent.putExtra("imgUrl", imgUrl);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        allBrowseAdapter.unregister();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_all_browse://所有浏览分类的那个
                //获取通知栏的高度
                Rect frame = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                PopupWindowView popupWindowView = new PopupWindowView
                        (context,((MainActivity)getActivity()).getHeight()+statusBarHeight);

                //把值传过去比较
                PopupWindowBean popupWindowBean = new PopupWindowBean();
                popupWindowBean.setIndex(0);
                popupWindowView.setBusBean(popupWindowBean);
                popupWindowView.initPopupWindow();
                break;
        }
    }
}
