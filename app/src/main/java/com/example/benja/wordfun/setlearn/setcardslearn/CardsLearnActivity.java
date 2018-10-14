package com.example.benja.wordfun.setlearn.setcardslearn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.mylib.Rotate3dListener;
import com.benjamin.mylib.Rotate3dView;
import com.example.benja.wordfun.R;
import com.example.benja.wordfun.setlearn.SetLearnActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class CardsLearnActivity extends AppCompatActivity {
    private ArrayList<SetCard> setCards;
    private TextToSpeech textToSpeech;
    private TextView progressText;
    private ImageView playImg,returnImg,shuffleImg;
    private Boolean isPlaying;
    private RecyclerView cardsRecyclerView;
    private PagerSnapHelper snapHelper;
    private ScrollSpeedLinearLayoutManger linearLayoutManager;
    private int curPosition;
    private Timer timer;
    private final static int RotateCurCard=2;
    private final static int UpdateFailed=3;
    private final static int UpdateSuccess=4;
    private OkHttpClient okHttpClient;
    private static final int listItemsMsg=1;
    private String author;
    private String TermSetsJson;

    private static class MyHandler extends Handler{

        private final WeakReference<CardsLearnActivity> mActivity;

        public MyHandler(CardsLearnActivity activity) {
            mActivity = new WeakReference<CardsLearnActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CardsLearnActivity activity=mActivity.get();
            if(activity!=null){
                switch (msg.what){
                    case RotateCurCard:
                        activity.curPosition=activity.getCurPosition();
                        final Rotate3dView curRotateView=(Rotate3dView) activity.snapHelper.findSnapView(activity.linearLayoutManager);
                        curRotateView.addRotate3dListener(new Rotate3dListener() {
                            @Override
                            public void onRotateEnd() {
                                if(activity.curPosition!=activity.setCards.size()-1){
                                    activity.playNextCard();
                                }else {
                                    activity.isPlaying=false;
                                    activity.playImg.setImageResource(R.drawable.vd_play);
                                    activity.timer.cancel();
                                    activity.timer=null;
                                }
                                curRotateView.removeRotate3dListener();
                            }
                        });
                        curRotateView.rotateCard(false);
                        break;
                    case listItemsMsg:
                        activity.TermSetsJson=msg.getData().getString("TermSetsJson");
                        activity.initCards(activity.author,activity.TermSetsJson);
                        activity.initViews();
                        break;
                    case UpdateFailed:
                        Toast.makeText(activity,"更新数据失败",Toast.LENGTH_LONG).show();
                        break;
                    case UpdateSuccess:
                        activity.finish();
                        break;
                }
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_learn);
        okHttpClient=new OkHttpClient();
        new Thread(new getTermListRunnable()).start();
    }

    private int getCurPosition(){
        View view=snapHelper.findSnapView(linearLayoutManager);
        int curPosition=linearLayoutManager.getPosition(view);
        return curPosition;
    }

    private void initViews(){
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setPitch(1f);
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        cardsRecyclerView=findViewById(R.id.cards_recyclerView);
        snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(cardsRecyclerView);
        linearLayoutManager=new ScrollSpeedLinearLayoutManger(this);
        final SetCardsAdapter setCardsAdapter=new SetCardsAdapter(this,textToSpeech,setCards);
        cardsRecyclerView.setAdapter(setCardsAdapter);
        cardsRecyclerView.setLayoutManager(linearLayoutManager);
        cardsRecyclerView.scrollToPosition(curPosition);
        cardsRecyclerView.scheduleLayoutAnimation();

        cardsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState==RecyclerView.SCROLL_STATE_SETTLING){
                    View view=snapHelper.findSnapView(linearLayoutManager);
                    int position=linearLayoutManager.getPosition(view);
                    progressText.setText(position+1+"/"+setCards.size());
                }
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    if(isPlaying){
                        playImg.setImageResource(R.drawable.vd_play);
                        isPlaying=false;
                        if(timer!=null){
                            timer.cancel();
                            timer=null;
                        }
                    }
                }
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    curPosition=getCurPosition();
                    progressText.setText(curPosition+1+"/"+setCards.size());
                }
            }
        });

        progressText=findViewById(R.id.cards_learn_progress_text);
        progressText.setText(curPosition+1+"/"+setCards.size());

        isPlaying=false;
        playImg=findViewById(R.id.cards_learn_play);
        playImg.setClickable(true);
        playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    playImg.setImageResource(R.drawable.vd_play);
                    isPlaying=false;
                    if(timer!=null){
                        timer.cancel();
                        timer=null;
                    }
                }else {
                    curPosition=getCurPosition();
                    if(curPosition!=setCards.size()-1){
                        playImg.setImageResource(R.drawable.vd_pause);
                        isPlaying=true;
                        startPlayCards();
                    }
                }
            }
        });

        returnImg=findViewById(R.id.cl_return);
        returnImg.setClickable(true);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shuffleImg=findViewById(R.id.cards_learn_shuffle);
        shuffleImg.setClickable(true);
        shuffleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(setCards);
                curPosition=0;
                cardsRecyclerView.scrollToPosition(curPosition);
                progressText.setText(curPosition+1+"/"+setCards.size());
                setCardsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void startPlayCards(){
        timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=RotateCurCard;
                mHandler.sendMessage(message);
            }
        };
        if(timer!=null){
            timer.schedule(timerTask,2000);
        }
    }

    private void playNextCard(){
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                cardsRecyclerView.smoothScrollToPosition(curPosition+1);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=RotateCurCard;
                        mHandler.sendMessage(message);
                    }
                },2000);
            }
        };
        if(timer!=null){
            timer.schedule(timerTask,2000);
        }
    }

    private class getTermListRunnable implements Runnable{
        @Override
        public void run() {
            Intent intent=getIntent();
            Bundle bundle=intent.getBundleExtra("setInfo");
            author=bundle.getString("author");
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

    private void initCards(String author,String TermSetsJson){
        setCards=new ArrayList<>();

        curPosition=0;
        try {
            JSONArray jsonArray=new JSONArray(TermSetsJson);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                boolean mflashed=(jsonObject.getInt("mflashed")==1);
                if(mflashed){
                    curPosition++;
                    String decodedTerm= URLDecoder.decode(jsonObject.getString("term"),"UTF-8").replace("<br>","\n");
                    String decodedDef= URLDecoder.decode(jsonObject.getString("definition"),"UTF-8").replace("<br>","\n");
                    long vid=jsonObject.getLong("vid");
                    SetCard setCard=new SetCard(decodedTerm,decodedDef);
                    setCard.setmflashed(mflashed);
                    setCard.setVid(vid);
                    setCard.setAuthor(author);
                    setCards.add(setCard);
                }
            }
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                boolean mflashed=(jsonObject.getInt("mflashed")==1);
                if(!mflashed){
                    String decodedTerm= URLDecoder.decode(jsonObject.getString("term"),"UTF-8").replace("<br>","\n");
                    String decodedDef= URLDecoder.decode(jsonObject.getString("definition"),"UTF-8").replace("<br>","\n");
                    long vid=jsonObject.getLong("vid");
                    SetCard setCard=new SetCard(decodedTerm,decodedDef);
                    setCard.setmflashed(mflashed);
                    setCard.setVid(vid);
                    setCard.setAuthor(author);
                    setCards.add(setCard);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void updateflashsToServer(){
        final Runnable r=new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                String url = "http://120.79.141.230:8080/api/updatemflashs";
                OkHttpClient client = new OkHttpClient();
                updateCardsBycurP(setCards,getCurPosition());
                Log.i("curPosition",curPosition+"");
                String jsonmflashs=getjsonflashs(setCards);
                Log.i("jsonmflashs",jsonmflashs);
                RequestBody body = RequestBody.create(JSON, jsonmflashs);
                Request request=new Request.Builder()
                        .url(url).post(body).build();
                Call call=client.newCall(request);
                try {
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message=new Message();
                            message.what=UpdateFailed;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message message=new Message();
                            message.what=UpdateSuccess;
                            mHandler.sendMessage(message);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    private void updateCardsBycurP(ArrayList<SetCard> setCards,int curPosition){
        for(int i=0;i<setCards.size();i++){
            setCards.get(i).setmflashed(false);
        }
        for(int i=0;i<setCards.size();i++){
            if(i<curPosition){
                setCards.get(i).setmflashed(true);
            }
        }
    }

    private String getjsonflashs(ArrayList<SetCard> setCards){
        String jsonflashs="";
        try {
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<setCards.size();i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("vid",setCards.get(i).getVid());
                if(setCards.get(i).ismflashed()){
                    jsonObject.put("mflashed",1);
                }else {
                    jsonObject.put("mflashed",0);
                }
                jsonObject.put("author",setCards.get(i).getAuthor());
                jsonArray.put(jsonObject);
            }
            jsonflashs=jsonArray.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonflashs;
    }

    @Override
    public void onBackPressed() {
        updateflashsToServer();
    }
}
