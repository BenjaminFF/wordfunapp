package com.example.benja.wordfun.setlist;

import android.view.View;

/**
 * Created by benja on 2018/3/14.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClickListener(View itemView,String title,String subTitle,String author,long createtime,int termCount);
}
