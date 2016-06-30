package com.example.dllo.mirror.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.databean.AllBean;

import java.util.List;

/**
 * Created by dllo on 16/6/24.
 */
public class OutAdapter extends BaseAdapter {
    private Context context;
    private List<AllBean.DataBean.ListBean.DataInfoBean.GoodsDataBean> goodsDataBeens;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    public OutAdapter(Context context) {
        this.context = context;
    }

    public void setGoodsDataBeens(List<AllBean.DataBean.ListBean.DataInfoBean.GoodsDataBean> goodsDataBeens) {
        this.goodsDataBeens = goodsDataBeens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return goodsDataBeens != null ? goodsDataBeens.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //拿到不同的布局
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }
    }

    //行布局总数
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OutListViewHolder outListViewHolder = null;
        OutListViewHolderOther outListViewHolderOther = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_ONE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_all_details_out_list_view, parent, false);
                    outListViewHolder = new OutListViewHolder(convertView);
                    convertView.setTag(outListViewHolder);
                    break;
                case TYPE_TWO:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_all_details_out_list_view_other, parent, false);
                    outListViewHolderOther = new OutListViewHolderOther(convertView);
                    convertView.setTag(outListViewHolderOther);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_ONE:
                    outListViewHolder = (OutListViewHolder) convertView.getTag();
                    break;
                case TYPE_TWO:
                    outListViewHolderOther = (OutListViewHolderOther) convertView.getTag();
                    break;
            }

        }

        switch (type) {
            case TYPE_ONE:
                outListViewHolder.introContent.setText(goodsDataBeens.get(position).getIntroContent());
                outListViewHolder.country.setText(goodsDataBeens.get(position).getCountry());
                outListViewHolder.english.setText(goodsDataBeens.get(position).getEnglish());
                outListViewHolder.location.setText(goodsDataBeens.get(position).getLocation());
                break;
            case TYPE_TWO:
                outListViewHolderOther.nameTv.setText(goodsDataBeens.get(position).getName());
                outListViewHolderOther.introContentTv.setText(goodsDataBeens.get(position).getIntroContent());
        }
        return convertView;
    }

    //position0
    class OutListViewHolder {
        TextView introContent, location, country, english;

        public OutListViewHolder(View itemView) {
            introContent = (TextView) itemView.findViewById(R.id.item_all_details_out_introContent_tv);
            location = (TextView) itemView.findViewById(R.id.item_all_details_out_location_tv);
            country = (TextView) itemView.findViewById(R.id.item_all_details_out_country_tv);
            english = (TextView) itemView.findViewById(R.id.item_all_details_out_english_tv);

        }
    }

    //position123
    class OutListViewHolderOther {
        TextView nameTv, introContentTv;

        public OutListViewHolderOther(View view) {
            nameTv = (TextView) view.findViewById(R.id.item_all_details_out_other_name_tv);
            introContentTv = (TextView) view.findViewById(R.id.item_all_details_out_other_introContent_tv);
        }
    }


}
