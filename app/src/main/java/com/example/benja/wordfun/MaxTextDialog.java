package com.example.benja.wordfun;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by benja on 2018/9/27.
 */

public class MaxTextDialog extends Dialog {
    private String maxText;
    private Context mContext;
    public MaxTextDialog(@NonNull Context context,String maxText) {
        super(context,R.style.MaxTextDialog);
        mContext=context;
        this.maxText=maxText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window dialogWindow=getWindow();
        WindowManager.LayoutParams layoutParams=dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        layoutParams.width=(int)(d.widthPixels*1);
        dialogWindow.setAttributes(layoutParams);

        setContentView(R.layout.dialog_maxtext);
        initView();
    }

    private void initView(){
        TextView textView=findViewById(R.id.maxTextView);
        textView.setText(maxText);

        FrameLayout container=findViewById(R.id.maxtext_dialog);
        container.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
