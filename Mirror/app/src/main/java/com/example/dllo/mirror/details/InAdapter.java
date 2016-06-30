package com.example.dllo.mirror.details;

import android.content.Context;
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
 * Created by dllo on 16/6/24.
 */
public class InAdapter extends BaseAdapter {
    private Context context;
    private List<AllBean.DataBean.ListBean.DataInfoBean.DesignDesBean> designDesBeens;
    private NetTools netTools;

    public InAdapter(Context context) {
        this.context = context;
        netTools = new NetTools();
    }

    public void setDesignDesBeens(List<AllBean.DataBean.ListBean.DataInfoBean.DesignDesBean> designDesBeens) {
        this.designDesBeens = designDesBeens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return designDesBeens != null ? designDesBeens.size() : 0;
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
        InListViewHolder inListViewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_details_in_list_view,parent,false);
            inListViewHolder = new InListViewHolder(convertView);
            convertView.setTag(inListViewHolder);
        }else {
            inListViewHolder = (InListViewHolder) convertView.getTag();
        }
        netTools.getImage(designDesBeens.get(position).getImg(),inListViewHolder.inImg,true);
        return convertView;
    }

    class InListViewHolder{
        ImageView inImg;

        public InListViewHolder(View itemView) {
            inImg = (ImageView) itemView.findViewById(R.id.item_all_details_in_list_view_img);
        }
    }
}
