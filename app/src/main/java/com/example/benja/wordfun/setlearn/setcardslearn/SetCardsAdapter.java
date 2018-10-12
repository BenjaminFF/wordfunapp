package com.example.benja.wordfun.setlearn.setcardslearn;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benjamin.mylib.Rotate3dListener;
import com.benjamin.mylib.Rotate3dView;
import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlearn.KeyValuePair;
import com.example.benja.wordfun.setlearn.SetInfoListAdapter;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by benja on 2018/9/28.
 */

public class SetCardsAdapter extends RecyclerView.Adapter<SetCardsAdapter.MyHolder>{

    private ArrayList<SetCard> setCards;
    Context mContext;
    TextToSpeech textToSpeech;

    public SetCardsAdapter(Context context,TextToSpeech textToSpeech,ArrayList<SetCard> setCards) {
        this.setCards = setCards;
        mContext=context;
        this.textToSpeech=textToSpeech;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        Rotate3dView rotate3dView;

        public MyHolder(View itemView) {
            super(itemView);
            rotate3dView=itemView.findViewById(R.id.card_rotate3dView);
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        MyHolder itemHolder = new MyHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String frontText=setCards.get(position).getTerm();
        String backText=setCards.get(position).getDefinition();
        int backgroundColor= SetUtil.getRandomColor(250);
        Rotate3dViewAdapter rotate3dViewAdapter=new Rotate3dViewAdapter(mContext,textToSpeech,frontText,backText,backgroundColor);
        holder.rotate3dView.setDuration(600);
        holder.rotate3dView.setAdapter(rotate3dViewAdapter);
        holder.setIsRecyclable(false);
    }



    @Override
    public int getItemCount() {
        return setCards.size();
    }
}
