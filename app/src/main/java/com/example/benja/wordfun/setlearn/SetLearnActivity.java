package com.example.benja.wordfun.setlearn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlist.SetListAdpter;
import com.example.benja.wordfun.setlist.SetListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SetLearnActivity extends AppCompatActivity {

    private static final int listItemsMsg=1;
    private TermListAdapter termListAdapter;
    private ArrayList<TermItem> termItems;
    private RecyclerView funcRecyclerView;
    private ImageView returnB,funcB;
    private boolean funcTurned;
    private OkHttpClient okHttpClient;
    private boolean isConnecting;
    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case listItemsMsg:
                    try {
                        termItems=getTermItems(msg.getData().getString("TermSetsJson"));
                        InitRecyclerView(termItems);
                        isConnecting=false;
                        swipeRefreshLayout.setRefreshing(false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_learn);
        okHttpClient=new OkHttpClient();
        new Thread(new getTermListRunnable()).start();
        isConnecting=true;
        initToolbar();
        funcRecyclerView=findViewById(R.id.setLearn_funcRecyclerView);
        FuncListAdapter funcListAdapter=new FuncListAdapter();
        funcRecyclerView.setAdapter(funcListAdapter);
        funcRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        swipeRefreshLayout=findViewById(R.id.termList_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isConnecting){
                    new Thread(new getTermListRunnable()).start();
                    isConnecting=true;
                }
            }
        });
    }

    private void initToolbar(){
        returnB=findViewById(R.id.setLearn_return);
        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        funcB=findViewById(R.id.setLearn_func);
        funcTurned=false;
        funcB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator;
                if(!funcTurned){
                     animator = ObjectAnimator.ofFloat(funcB, "rotation", 0f, 90f);
                     showFuncs();
                     funcTurned=true;
                }else {
                    animator = ObjectAnimator.ofFloat(funcB, "rotation", 90f, 0f);
                    funcTurned=false;
                    dismissFuncs();
                }
                animator.setDuration(500);
                animator.start();
            }
        });
    }

    private void showFuncs(){
        if(funcRecyclerView.getVisibility()==View.INVISIBLE){
            funcRecyclerView.setVisibility(View.VISIBLE);
        }
        float curTranslationX=funcRecyclerView.getTranslationX();
        float offset=funcRecyclerView.getHeight();
        ObjectAnimator animator=ObjectAnimator.ofFloat(funcRecyclerView,"translationY",offset,curTranslationX);
        animator.setDuration(500);
        animator.start();
    }

    private void dismissFuncs(){
        float curTranslationX=funcRecyclerView.getTranslationX();
        float offset=funcRecyclerView.getHeight();
        ObjectAnimator animator=ObjectAnimator.ofFloat(funcRecyclerView,"translationY",curTranslationX,offset);
        animator.setDuration(500);
        animator.start();
    }

    private ArrayList<TermItem> getTermItems(String json){
        ArrayList<TermItem> termItems=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                TermItem termItem=new TermItem();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String decodedTerm= URLDecoder.decode(jsonObject.getString("term"),"UTF-8").replace("<br>","\n");
                String decodedDef= URLDecoder.decode(jsonObject.getString("definition"),"UTF-8").replace("<br>","\n");
                termItem.setTermText(decodedTerm);
                termItem.setDefText(decodedDef);
                termItem.setmMatrixed(jsonObject.getInt("mmatrixed")==1);
                termItem.setmWrited(jsonObject.getInt("mwrited")==1);
                termItems.add(termItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return termItems;
    }

    private void InitRecyclerView(ArrayList<TermItem> termItems){
        RecyclerView recyclerView=findViewById(R.id.termList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        termListAdapter=new TermListAdapter(termItems,this);
        recyclerView.setAdapter(termListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private class getTermListRunnable implements Runnable{
        @Override
        public void run() {
            Intent intent=getIntent();
            Bundle bundle=intent.getBundleExtra("setInfo");
            String author=bundle.getString("author");
            Long createTime=bundle.getLong("createTime");
            String url = "http://192.168.43.219:8088/api/getmCards?username="+author+"&createTime="+createTime;
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Message message=new Message();
                        message.what=listItemsMsg;
                        Bundle bundle=new Bundle();
                        //Log.i("ATest",response.body().string());
                        bundle.putString("TermSetsJson",response.body().string());
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
