package com.example.dllo.mirror.wearpicture;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.databean.AllBean;
import com.example.dllo.mirror.net.NetTools;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dllo on 16/6/25.
 */
public class WearPictureAdapter extends BaseAdapter {
    private List<AllBean.DataBean.ListBean.DataInfoBean.WearVideoBean> wearVideoBeens;
    private Context context;
    private NetTools netTools;

    public WearPictureAdapter(Context context) {
        this.context = context;
        netTools = new NetTools();
    }

    public void setWearVideoBeens(List<AllBean.DataBean.ListBean.DataInfoBean.WearVideoBean> wearVideoBeens) {
        //把type为8的删除
        for (int i = 0; i < wearVideoBeens.size(); i++) {
            if ("8".equals(wearVideoBeens.get(i).getType())) {
                wearVideoBeens.remove(wearVideoBeens.get(i));
            }
        }
        //把type为9的删除
        for (int i = 0; i < wearVideoBeens.size(); i++) {
            if ("9".equals(wearVideoBeens.get(i).getType())) {
                wearVideoBeens.remove(wearVideoBeens.get(i));
            }
        }
        this.wearVideoBeens = wearVideoBeens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return wearVideoBeens != null ? wearVideoBeens.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WearHolder wearHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wear_picture, parent, false);
            wearHolder = new WearHolder(convertView);
            convertView.setTag(wearHolder);
        } else {
            wearHolder = (WearHolder) convertView.getTag();
        }

        //        //设置适配屏幕宽的代码
//        ViewGroup.LayoutParams params = wearHolder.img.getLayoutParams();
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        params.width = windowManager.getDefaultDisplay().getWidth();
//        wearHolder.img.setLayoutParams(params);

        netTools.getImage(wearVideoBeens.get(position).getData(), wearHolder.img, true);
        return convertView;
    }

    class WearHolder {
        ImageView img;

        public WearHolder(View itemView) {
            img = (ImageView) itemView.findViewById(R.id.item_wear_picture_img);

        }
    }

}
