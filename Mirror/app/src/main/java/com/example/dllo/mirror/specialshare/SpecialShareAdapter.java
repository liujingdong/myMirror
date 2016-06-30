package com.example.dllo.mirror.specialshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.squareup.picasso.Picasso;

/**
 * Created by dllo on 16/6/24.
 */
public class SpecialShareAdapter extends RecyclerView.Adapter<SpecialShareAdapter.SpecialShareHolder> {
    private Context context;
    private SpecialShareBean specialShareBean;

    public SpecialShareAdapter(Context context) {
        this.context = context;
    }

    public void setSpecialShareBean(SpecialShareBean specialShareBean) {
        this.specialShareBean = specialShareBean;
        notifyDataSetChanged();
    }

    @Override
    public SpecialShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_special_share,parent,false);
        SpecialShareHolder specialShareHolder = new SpecialShareHolder(view);
        return specialShareHolder;
    }

    @Override
    public void onBindViewHolder(SpecialShareHolder holder, int position) {
        holder.storyTitle.setText(specialShareBean.getData().getList().get(position).getStory_title());
        Picasso.with(context).load(specialShareBean.getData().getList().get(position).getStory_img()).fit().into(holder.storyImg);
    }

    @Override
    public int getItemCount() {
        return specialShareBean != null ? specialShareBean.getData().getList().size() : 0;
    }

    class SpecialShareHolder extends RecyclerView.ViewHolder {
        ImageView storyImg;
        TextView storyTitle;

        public SpecialShareHolder(View itemView) {
            super(itemView);
            storyImg = (ImageView) itemView.findViewById(R.id.item_special_share_img);
            storyTitle = (TextView) itemView.findViewById(R.id.item_special_share_title);
        }
    }
}
