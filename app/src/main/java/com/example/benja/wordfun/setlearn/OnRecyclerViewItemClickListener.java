package com.example.benja.wordfun.setlearn;

import android.view.View;

/**
 * Created by benja on 2018/3/14.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClickListener(View itemView, String author, long createtime);
    void onItemClickListener(View itemView,String title);
}
