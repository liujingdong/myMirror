package com.example.dllo.mirror.wearpicture;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.example.dllo.mirror.R;
import com.example.dllo.mirror.base.BaseActivity;
import com.example.dllo.mirror.databean.AllBean;
import com.example.dllo.mirror.net.NetListener;
import com.example.dllo.mirror.net.NetTools;
import com.example.dllo.mirror.net.URLValues;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dllo on 16/6/25.
 */
public class WearPictureActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;
    private ImageView backImg;
    private TextView buyTv;
    private WearPictureAdapter wearPictureAdapter;
    private NetTools netTools;
    private int position;
    private List<AllBean.DataBean.ListBean.DataInfoBean.WearVideoBean> wearVideoBeens;
    //视频播放
    private VideoView videoView;//视频播放
    private ImageView videoImg;
    private ImageView playImg;
    private String videoUri;//视频网址
    private String firstUrl;//第一个图片的网址
    //动画
    private int[] location;

    @Override
    public int mSetContentView() {
        return R.layout.activity_wear_picture;
    }

    @Override
    public void initView() {
        netTools = new NetTools();
        wearPictureAdapter = new WearPictureAdapter(this);
        listView = findView(R.id.activity_wear_picture_list_view);
        backImg = findView(R.id.activity_wear_picture_back_img);
        buyTv = findView(R.id.activity_wear_picture_buy_tv);
        backImg.setOnClickListener(this);
        buyTv.setOnClickListener(this);
        listView.setAdapter(wearPictureAdapter);

    }

    @Override
    public void initData() {
        //加头布局
        View view = LayoutInflater.from(this).inflate(R.layout.head_wear_picture, null);
        listView.addHeaderView(view);
        videoView = (VideoView) view.findViewById(R.id.head_wear_picture_video_view);
        videoImg = (ImageView) view.findViewById(R.id.head_wear_picture_img);
        playImg = (ImageView) view.findViewById(R.id.head_wear_picture_play);
        //点击事件
        playImg.setOnClickListener(this);


        //DetailsActivity过来的
        Intent intent = getIntent();
        position = intent.getIntExtra("pos", 0);

        //解析数据
        //请求体
        String body = "last_time=&device_type=3&page=&token=&version=1.0.1";
        //请求头
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/x-www-form-urlencoded");
        head.put("Content-Length", "51");
        head.put("Host", "api.mirroreye.cn");
        head.put("Connection", "Keep-Alive");
        head.put("Cookie", "PHPSESSID=0q0374jcg8m9so617bu65b1qm3; cp_language=zhUser-Agent");
        netTools.getData(URLValues.ALL_URL, new NetListener() {
            @Override
            public void onSuccessed(String string) {
                Gson gson = new Gson();
                AllBean allBean = gson.fromJson(string, AllBean.class);
                wearVideoBeens = allBean.getData().getList().get(position).getData_info().getWear_video();

                //把type为8的找到
                for (int i = 0; i < wearVideoBeens.size(); i++) {
                    if ("8".equals(wearVideoBeens.get(i).getType())) {
                        videoUri = wearVideoBeens.get(i).getData();
                    } else if ("9".equals(wearVideoBeens.get(i).getType())) {
                        //把type为9的找到,拿来做视频背景
                        firstUrl = wearVideoBeens.get(i).getData();
                    }
                }

                Picasso.with(WearPictureActivity.this).load(firstUrl).fit().into(videoImg);

                wearPictureAdapter.setWearVideoBeens(wearVideoBeens);
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        }, head, body);

        //ListView 的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(WearPictureActivity.this, ScaleActivity.class);
                intent1.putExtra("imgUrl", wearVideoBeens.get(position - 1).getData());
                location = new int[2];
                view.getLocationOnScreen(location);
                intent1.putExtra("locationX", location[0]);//必须
                intent1.putExtra("locationY", location[1]);//必须

                intent1.putExtra("width", view.getWidth());//必须
                intent1.putExtra("height", view.getHeight());//必须
                startActivity(intent1);
                overridePendingTransition(0, 0);
            }
        });

    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_wear_picture_back_img://返回
                finish();
                break;
            case R.id.activity_wear_picture_buy_tv://购买
                break;
            case R.id.head_wear_picture_play://播放视频
                videoView.start();
                playImg.setVisibility(View.GONE);
                videoImg.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                playVideo(videoUri, firstUrl);
                break;
        }
    }

    //头布局视频播放的方法
    public void playVideo(String videoUri, final String firstUrl) {
        Uri uri = Uri.parse(videoUri);
        videoView.setVideoURI(uri);
        //提供一个控制器，控制其暂停、播放……等功能
        videoView.setMediaController(new MediaController(this));
        //视频或者音频到结尾时触发的方法
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(WearPictureActivity.this, "播放完事啦", Toast.LENGTH_SHORT).show();
                playImg.setVisibility(View.VISIBLE);
                videoImg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                Picasso.with(WearPictureActivity.this).load(firstUrl).fit().into(videoImg);
            }
        });
    }
}
