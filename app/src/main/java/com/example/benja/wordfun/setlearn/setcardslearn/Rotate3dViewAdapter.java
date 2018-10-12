package com.example.benja.wordfun.setlearn.setcardslearn;

import android.content.Context;
import android.graphics.Outline;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.benjamin.mylib.Rotate3dView;
import com.example.benja.wordfun.MaxTextDialog;
import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlearn.SetInfoListAdapter;

import java.util.Locale;

/**
 * Created by benja on 2018/9/28.
 */

public class Rotate3dViewAdapter extends Rotate3dView.Adapter<Rotate3dViewAdapter.MyFrontHolder,Rotate3dViewAdapter.MyBackHolder> {

    private String frontText;
    private String backText;
    private int backgroundColor;
    private TextToSpeech textToSpeech;
    private Context mContext;

    public Rotate3dViewAdapter(Context context,TextToSpeech textToSpeech,String frontText, String backText, int backgroundColor) {
        this.frontText = frontText;
        this.backText = backText;
        this.backgroundColor = backgroundColor;
        this.textToSpeech=textToSpeech;
        mContext=context;
    }

    class MyFrontHolder extends Rotate3dView.ViewHolder{
        TextView textView;
        ImageView labaImg;
        public MyFrontHolder(View itemView) {
            super(itemView);
            ((CardView)itemView).setCardBackgroundColor(backgroundColor);
            textView=itemView.findViewById(R.id.card_front_text);
            labaImg=itemView.findViewById(R.id.front_laba);
        }
    }

    class MyBackHolder extends Rotate3dView.ViewHolder{
        TextView textView;
        ImageView labaImg;
        public MyBackHolder(View itemView) {
            super(itemView);
            ((CardView)itemView).setCardBackgroundColor(backgroundColor);
            textView=itemView.findViewById(R.id.card_back_text);
            labaImg=itemView.findViewById(R.id.back_laba);
        }
    }

    @Override
    public MyFrontHolder onCreateFrontViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_front, viewGroup, false);
        final MyFrontHolder itemHolder = new MyFrontHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindFrontViewHolder(MyFrontHolder myFrontHolder) {
        myFrontHolder.textView.setText(frontText);
        myFrontHolder.textView.setClickable(true);
        myFrontHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MaxTextDialog maxTextDialog=new MaxTextDialog(mContext,frontText);
                maxTextDialog.show();
                return false;
            }
        });
        myFrontHolder.labaImg.setClickable(true);
        myFrontHolder.labaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textToSpeech.isSpeaking()){
                    textToSpeech.stop();
                }
                textToSpeech.speak(frontText,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    public MyBackHolder onCreateBackViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_back, viewGroup, false);
        final MyBackHolder itemHolder = new MyBackHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindBackViewHolder(MyBackHolder myBackHolder) {
        myBackHolder.textView.setText(backText);
        myBackHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MaxTextDialog maxTextDialog=new MaxTextDialog(mContext,backText);
                maxTextDialog.show();
                return false;
            }
        });
        myBackHolder.labaImg.setClickable(true);
        myBackHolder.labaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textToSpeech.isSpeaking()){
                    textToSpeech.stop();
                }
                textToSpeech.speak(backText,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }
}
