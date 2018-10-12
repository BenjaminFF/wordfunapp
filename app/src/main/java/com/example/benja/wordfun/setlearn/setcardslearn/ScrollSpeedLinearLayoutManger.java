package com.example.benja.wordfun.setlearn.setcardslearn;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by benja on 2018/10/3.
 */

public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {

    public ScrollSpeedLinearLayoutManger(Context context) {
        super(context, VERTICAL, false);
    }

    public ScrollSpeedLinearLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return ScrollSpeedLinearLayoutManger.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 0.2f;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}
