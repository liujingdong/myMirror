package com.example.dllo.mirror.allbrowse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.databean.AllBean;
import com.example.dllo.mirror.databean.AllType2Bean;
import com.example.dllo.mirror.databean.ParserDataBean;
import com.example.dllo.mirror.net.NetTools;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dllo on 16/6/21.
 */
public class AllBrowseAdapter extends RecyclerView.Adapter {
    private Context context;
    private AllBean allBean;
    private AllType2Bean allType2Bean;
    private NetTools netTools;
    private static final int ONE = 1, TWO = 2;
    private ParserDataBean parserDataBean;//解决Fragment销毁后没有数据的Bug
    //点击事件
    private AllBrowseOnClickListener onClickListener;

    //把借口传进来
    public void setOnClickListener(AllBrowseOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AllBrowseAdapter(Context context) {
        this.context = context;
        parserDataBean = new ParserDataBean();//解决Fragment销毁后没有数据的Bug
        //注册EventBus(记得取消注册哈宝宝们)
        EventBus.getDefault().register(this);
        netTools = new NetTools();
    }

    //拿到我的AllBean
    @Subscribe
    public void setAllBean(AllBean allBean) {
        this.allBean = allBean;
        notifyDataSetChanged();
    }

    //拿到我的AllType2Bean
    @Subscribe
    public void setAllType2Bean(AllType2Bean allType2Bean) {
        this.allType2Bean = allType2Bean;
    }

    //取消EventBus的注册方法
    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    //该方法返回position行布局的类型
    @Override
    public int getItemViewType(int position) {
        int type = Integer.valueOf(allBean.getData().getList().get(position).getType());
        return type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        //根据ViewType绑定不同的holder
        switch (viewType) {
            case ONE:
                View view = LayoutInflater.from(context).inflate(R.layout.item_all_browse_one, parent, false);
                holder = new AllBrowseHolder(view);
                break;
            case TWO:
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_all_browse_two, parent, false);
                holder = new AllBrowseHolderTwo(itemView);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AllBean.DataBean.ListBean.DataInfoBean bean = allBean.getData().getList().get(position).getData_info();
        int type = getItemViewType(position);
        switch (type){
            case ONE:
                AllBrowseHolder allBrowseHolder = (AllBrowseHolder) holder;
                allBrowseHolder.goodsName.setText(bean.getGoods_name());
                allBrowseHolder.goodsPrice.setText(bean.getGoods_price());
                allBrowseHolder.brand.setText(bean.getBrand());
                allBrowseHolder.productArea.setText(bean.getProduct_area());
                netTools.getImage(bean.getGoods_img(), allBrowseHolder.img,true);
                //点击事件
                if(onClickListener!=null){
                    allBrowseHolder.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String imgurl =
                                    allBean.getData().getList().get(position).getData_info().getGoods_img();
                            onClickListener.onClick(imgurl,1,position);
                        }
                    });
                }
                break;
            case TWO:
                AllBrowseHolderTwo allBrowseHolderTwo = (AllBrowseHolderTwo) holder;
                allBrowseHolderTwo.storyTitle.setText(allType2Bean.getData().getList().get(position).getData_info().getStory_title());
                Picasso.with(context).load(allType2Bean.getData().getList().get(position).getData_info().getStory_img()).fit()
                        .into(allBrowseHolderTwo.storyImg);
                //点击事件
                if(onClickListener!=null){
                    allBrowseHolderTwo.storyImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String imgUrl = allType2Bean.getData().getList().get(position).getData_info().getStory_url();
                            onClickListener.onClick(imgUrl,2,position);
                        }
                    });
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return allBean != null ? allBean.getData().getList().size() : 0;
    }

    //type为1的时候布局
    class AllBrowseHolder extends RecyclerView.ViewHolder {
        TextView goodsName, goodsPrice, productArea, brand;
        ImageView img;

        public AllBrowseHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            goodsName = (TextView) itemView.findViewById(R.id.item_all_browse_goods_name);
            goodsPrice = (TextView) itemView.findViewById(R.id.item_all_browse_goods_price);
            productArea = (TextView) itemView.findViewById(R.id.item_all_browse_product_area);
            brand = (TextView) itemView.findViewById(R.id.item_all_browse_brand);
            img = (ImageView) itemView.findViewById(R.id.item_all_browse_goods_img);
        }
    }

    //type为2的时候布局
    class AllBrowseHolderTwo extends RecyclerView.ViewHolder {
        TextView storyTitle;
        ImageView storyImg;

        public AllBrowseHolderTwo(View itemView) {
            super(itemView);
            storyTitle = (TextView) itemView.findViewById(R.id.item_all_browse_two_story_title);
            storyImg = (ImageView) itemView.findViewById(R.id.item_all_browse_two_story_img);
        }
    }

}
