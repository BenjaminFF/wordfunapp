package com.example.benja.wordfun.setlist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Base64;
import android.widget.Toast;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlearn.SetLearnActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetListFragment extends Fragment {


    public SetListFragment() {
        // Required empty public constructor
    }

    private final static int listItemsMsg=1;
    private ArrayList<ListItem> listItems;
    private SetListAdpter setListAdpter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView netHintImg;
    private boolean isConnecting;
    OkHttpClient okHttpClient;
    View mView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case listItemsMsg:
                    try {
                        if(msg.getData().getBoolean("connectSucceed")){
                            listItems=getListFromJson(msg.getData().getString("jsonSets"));
                            InitRecyclerView(listItems,mView);
                            netHintImg.setVisibility(View.GONE);
                        }else {
                            if(netHintImg.getVisibility()!=View.VISIBLE){
                                netHintImg.setVisibility(View.VISIBLE);
                            }
                        }
                        isConnecting=false;
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_set_list, container, false);
        mView=v;
        swipeRefreshLayout=v.findViewById(R.id.setList_refresh_layout);
        netHintImg=v.findViewById(R.id.setList_netHint_img);
        okHttpClient=new OkHttpClient();
        //先判断网络是否连接，如果未连接
        if(SetUtil.isNetworkAvailable(getContext())){
            netHintImg.setVisibility(View.GONE);
            new Thread(new getListRunnable()).start();
            isConnecting=true;
        }else {
            netHintImg.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isConnecting){
                    new Thread(new getListRunnable()).start();
                    isConnecting=true;
                }
            }
        });
        return v;
    }

    private class getListRunnable implements Runnable{
        @Override
        public void run() {
            String url = "http://192.168.43.219:8088/api/getwordset?username=benjamin";
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("onFailure", "onFailure: ");
                    Message message=new Message();
                    message.what=listItemsMsg;
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("connectSucceed",false);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Message message=new Message();
                        message.what=listItemsMsg;
                        Bundle bundle=new Bundle();
                        bundle.putBoolean("connectSucceed",true);
                        bundle.putString("jsonSets",response.body().string());
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private ArrayList<ListItem> getListFromJson(String json) throws Exception{
        ArrayList<ListItem> listItems=new ArrayList<>();
        JSONObject outer=new JSONObject(json);
        JSONArray sets=outer.getJSONArray("sets");
        for(int i=0;i<sets.length();i++){
            ListItem item=new ListItem();
            JSONObject itemJson=sets.getJSONObject(i);
            String decodedTitle= URLDecoder.decode(itemJson.getString("title"),"UTF-8");
            item.setTitle(new String(decodedTitle));
            item.setAuthor(itemJson.getString("author"));
            item.setFolder(itemJson.getString("folder"));
            item.setTermCount(itemJson.getInt("termCount"));
            item.setCreateTime(itemJson.getLong("createtime"));
            listItems.add(item);
        }
        return listItems;
    }

    private void InitRecyclerView(ArrayList<ListItem> listItems,View parentView){
        RecyclerView recyclerView=parentView.findViewById(R.id.setList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(parentView.getContext());
        setListAdpter=new SetListAdpter(listItems);
        recyclerView.setAdapter(setListAdpter);
        recyclerView.setLayoutManager(linearLayoutManager);
        setListAdpter.addRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, String author, long createtime) {
                Intent intent=new Intent(getActivity(), SetLearnActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("author",author);
                bundle.putLong("createTime",createtime);
                intent.putExtra("setInfo",bundle);
                startActivity(intent);
            }
        });
    }
}
