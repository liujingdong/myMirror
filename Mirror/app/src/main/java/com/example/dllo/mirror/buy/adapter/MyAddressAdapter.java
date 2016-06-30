package com.example.dllo.mirror.buy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dllo.mirror.R;
import com.example.dllo.mirror.buy.bean.MyAddressBean;

/**
 * Created by dllo on 16/6/29.
 */
public class MyAddressAdapter extends BaseAdapter {
    private MyAddressBean myAddressBean;
    private Context context;

    public MyAddressAdapter(Context context) {
        this.context = context;
    }

    public void setMyAddressBean(MyAddressBean myAddressBean) {
        this.myAddressBean = myAddressBean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myAddressBean != null ? myAddressBean.getData().getList().size() : 0;
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
        MyAddressHolder myAddressHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_address, parent, false);
            myAddressHolder = new MyAddressHolder(convertView);
            convertView.setTag(myAddressHolder);
        } else {
            myAddressHolder = (MyAddressHolder) convertView.getTag();
        }
        myAddressHolder.username.setText("收件人: " + myAddressBean.getData().getList().get(position).getUsername());
        myAddressHolder.cellphone.setText("地址: " + myAddressBean.getData().getList().get(position).getCellphone());
        myAddressHolder.addr_info.setText("联系电话: " + myAddressBean.getData().getList().get(position).getAddr_info());
        return convertView;
    }

    class MyAddressHolder {
        TextView username, addr_info, cellphone;

        public MyAddressHolder(View view) {
            username = (TextView) view.findViewById(R.id.item_my_address_username);
            addr_info = (TextView) view.findViewById(R.id.item_my_address_addr_info);
            cellphone = (TextView) view.findViewById(R.id.item_my_address_cellphone);
        }
    }
}
