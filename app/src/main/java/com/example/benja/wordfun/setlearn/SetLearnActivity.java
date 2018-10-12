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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlearn.setcardslearn.CardsLearnActivity;
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
    private String TermSetsJson;
    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case listItemsMsg:
                    try {
                        TermSetsJson=msg.getData().getString("TermSetsJson");
                        termItems=getTermItems(TermSetsJson);
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

        initViews();
    }

    private void initViews(){

        funcRecyclerView=findViewById(R.id.setLearn_funcRecyclerView);
        FuncListAdapter funcListAdapter=new FuncListAdapter();
        funcRecyclerView.setAdapter(funcListAdapter);
        funcRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        funcListAdapter.addRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View itemView, String author, long createtime) {

            }

            @Override
            public void onItemClickListener(View itemView, String title) {
                if(title.equals("信息")){
                    showInfoDialog();
                }else if(title.equals("单词卡")){
                    openCardsLearn();
                }
            }
        });

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

    private void showInfoDialog(){
        ArrayList<KeyValuePair<String,String>> setInfoItems=new ArrayList<>();
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("setInfo");
        String setInfoTitle=bundle.getString("title");
        String setInfosubTitle=bundle.getString("subTitle");
        String author=bundle.getString("author");
        int termCount=bundle.getInt("termCount");
        String createTime=(String) DateFormat.format("yyyy/MM/dd",bundle.getLong("createTime"));
        setInfoItems.add(new KeyValuePair<String, String>("标题",setInfoTitle));
        setInfoItems.add(new KeyValuePair<String, String>("副标题",setInfosubTitle));
        setInfoItems.add(new KeyValuePair<String, String>("作者",author));
        setInfoItems.add(new KeyValuePair<String, String>("单词集数量",termCount+""));
        setInfoItems.add(new KeyValuePair<String, String>("创建时间",createTime+""));
        Log.i("setInfoTitle",setInfoItems.size()+"");
        SetInfoDialog setInfoDialog=new SetInfoDialog(this,setInfoItems);
        setInfoDialog.show();
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
        recyclerView.scheduleLayoutAnimation();
    }

    private class getTermListRunnable implements Runnable{
        @Override
        public void run() {
            Intent intent=getIntent();
            Bundle bundle=intent.getBundleExtra("setInfo");
            String author=bundle.getString("author");
            Long createTime=bundle.getLong("createTime");
            String url = "http://120.79.141.230:8080/api/getmCards?username="+author+"&createTime="+createTime;
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

    private void openCardsLearn(){
        Intent intent=getIntent();
        Intent launchIntent=new Intent(SetLearnActivity.this,CardsLearnActivity.class);
        Bundle bundle=intent.getBundleExtra("setInfo");
        launchIntent.putExtra("setInfo",bundle);
        startActivity(launchIntent);
    }
}
