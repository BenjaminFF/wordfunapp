package com.example.benja.wordfun.setlearn;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benja.wordfun.R;
import com.example.benja.wordfun.SetUtil;
import com.example.benja.wordfun.setlist.SetListAdpter;

import java.util.ArrayList;

/**
 * Created by benja on 2018/9/14.
 */

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ItemHolder>{

    private ArrayList<TermItem> termItems;
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

    public TermListAdapter(ArrayList<TermItem> termItems) {
        this.termItems = termItems;
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

        final String defText=termItems.get(position).getDefText();
        holder.definition.setText(defText);

    }

    @Override
    public int getItemCount() {
        return termItems.size();
    }
}
