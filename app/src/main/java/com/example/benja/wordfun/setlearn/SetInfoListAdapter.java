package com.example.benja.wordfun.setlearn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benja.wordfun.R;

import java.util.ArrayList;

/**
 * Created by benja on 2018/9/27.
 */

public class SetInfoListAdapter extends RecyclerView.Adapter<SetInfoListAdapter.MyHolder> {


    private ArrayList<KeyValuePair<String,String>> setInfoItems;
    class MyHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        public MyHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.setInfo_Item_Title);
            content=itemView.findViewById(R.id.setInfo_Item_content);
        }
    }

    public SetInfoListAdapter(ArrayList<KeyValuePair<String, String>> setInfoItems) {
        this.setInfoItems = setInfoItems;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.setinfo_item, parent, false);
        final SetInfoListAdapter.MyHolder itemHolder = new  SetInfoListAdapter.MyHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.title.setText(setInfoItems.get(position).key);
        holder.content.setText(setInfoItems.get(position).value);
    }

    @Override
    public int getItemCount() {
        return setInfoItems.size();
    }
}
