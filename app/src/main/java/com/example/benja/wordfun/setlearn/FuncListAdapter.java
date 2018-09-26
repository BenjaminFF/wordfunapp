package com.example.benja.wordfun.setlearn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by benja on 2018/9/25.
 */

public class FuncListAdapter extends RecyclerView.Adapter<FuncListAdapter.MyHolder> {
    ArrayList<KeyValuePair<Integer,String>> funcs;

    public FuncListAdapter() {
        funcs=new ArrayList<>();
        funcs.add(new KeyValuePair<Integer, String>(R.drawable.vd_pen,"书写"));
        funcs.add(new KeyValuePair<Integer, String>(R.drawable.vd_card,"单词卡"));
        funcs.add(new KeyValuePair<Integer, String>(R.drawable.vd_matrix,"矩阵"));
        funcs.add(new KeyValuePair<Integer, String>(R.drawable.vd_test,"测试"));
        funcs.add(new KeyValuePair<Integer, String>(R.drawable.vd_info,"信息"));
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.sl_func_img);
            textView=itemView.findViewById(R.id.sl_fun_text);
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_setlearn_func, parent, false);
        final MyHolder itemHolder = new MyHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.imageView.setImageResource(funcs.get(position).key);
        holder.textView.setText(funcs.get(position).value);
    }

    @Override
    public int getItemCount() {
        return funcs.size();
    }
}
