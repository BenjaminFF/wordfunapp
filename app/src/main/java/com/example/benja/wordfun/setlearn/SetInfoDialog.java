package com.example.benja.wordfun.setlearn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.benja.wordfun.R;

import java.util.ArrayList;

/**
 * Created by benja on 2018/9/27.
 */

public class SetInfoDialog extends Dialog {

    private Context mContext;
    private ArrayList<KeyValuePair<String,String>> setInfoItems;

    public SetInfoDialog(@NonNull Context context,ArrayList<KeyValuePair<String,String>> setInfoItems) {
        super(context, R.style.SetInfoDialog);
        mContext=context;
        this.setInfoItems=setInfoItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSize(0.9f,0.8f);

        setContentView(R.layout.dialog_setinfo);
        initViews();
    }

    private void setSize(float wPercentage,float hPercentage){
        Window dialogWindow=getWindow();
        WindowManager.LayoutParams layoutParams=dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        layoutParams.width=(int)(d.widthPixels*wPercentage);
        layoutParams.height=(int)(d.heightPixels*hPercentage);
        dialogWindow.setAttributes(layoutParams);
    }

    private void initViews(){
        RecyclerView recyclerView=findViewById(R.id.setInfo_recyclerView);
        SetInfoListAdapter setInfoListAdapter=new SetInfoListAdapter(setInfoItems);
        recyclerView.setAdapter(setInfoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }
}
