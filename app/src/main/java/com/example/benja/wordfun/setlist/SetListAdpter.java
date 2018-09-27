package com.example.benja.wordfun.setlist;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;

import java.util.ArrayList;

/**
 * Created by benja on 2018/9/10.
 */

public class SetListAdpter extends RecyclerView.Adapter<SetListAdpter.ItemHolder> {

    private ArrayList<ListItem> listItems;
    private OnRecyclerViewItemClickListener recyclerViewItemClickListener;

    public SetListAdpter(ArrayList<ListItem> listItems) {
        this.listItems = listItems;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

            private TextView title;
            private TextView createTime;
            private TextView termCount;
            public ItemHolder(View itemView) {
                super(itemView);
                ((CardView)itemView).setCardBackgroundColor(SetUtil.getRandomColor(220));
                title=itemView.findViewById(R.id.listItem_title);
                createTime=itemView.findViewById(R.id.listItem_createTime);
                termCount=itemView.findViewById(R.id.listItem_count);
            }
        }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_set_item, parent, false);
        final ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {

        final String title=listItems.get(position).getTitle();
        int itemCount=listItems.get(position).getTermCount();
        final long createTime=listItems.get(position).getCreateTime();
        holder.title.setText(title);
        holder.createTime.setText(DateFormat.format("yyyy/MM/dd",createTime));
        holder.termCount.setText(itemCount+" terms");
        final String author=listItems.get(position).getAuthor();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewItemClickListener.onItemClickListener(holder.itemView,author,createTime);
            }
        });
        //holder.createTime.setText(createTime);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void addRecyclerViewItemClickListener(OnRecyclerViewItemClickListener recyclerViewItemClickListener){
        this.recyclerViewItemClickListener=recyclerViewItemClickListener;
    }
}
