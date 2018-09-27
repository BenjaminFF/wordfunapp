package com.example.benja.wordfun.setlearn;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benja.wordfun.MaxTextDialog;
import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;

import java.util.ArrayList;

/**
 * Created by benja on 2018/9/14.
 */

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ItemHolder>{

    private ArrayList<TermItem> termItems;
    private Context mContext;
    public class ItemHolder extends RecyclerView.ViewHolder{

        private TextView term;
        private TextView definition;
        public ItemHolder(View itemView) {
            super(itemView);
            ((CardView)itemView).setCardBackgroundColor(SetUtil.getRandomColor(255));
            term=itemView.findViewById(R.id.termText);
            definition=itemView.findViewById(R.id.defText);
        }
    }

    public TermListAdapter(ArrayList<TermItem> termItems,Context context) {
        this.termItems = termItems;
        mContext=context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_term_item, parent, false);
        final TermListAdapter.ItemHolder itemHolder = new TermListAdapter.ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final String termText=termItems.get(position).getTermText();
        holder.term.setText(termText);
        holder.term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxTextDialog maxTextDialog=new MaxTextDialog(mContext,termText);
                maxTextDialog.show();
            }
        });

        final String defText=termItems.get(position).getDefText();
        holder.definition.setText(defText);
        holder.definition.setClickable(true);
        holder.definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxTextDialog maxTextDialog=new MaxTextDialog(mContext,defText);
                maxTextDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return termItems.size();
    }
}
