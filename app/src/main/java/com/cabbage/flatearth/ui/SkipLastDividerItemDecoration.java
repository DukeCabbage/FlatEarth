package com.cabbage.flatearth.ui;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Leo, July 11, 2018
 * <p>
 * This is basically a copy paste of the "built-in" version of  DividerItemDecoration in
 * android.support.v7.widget. The only difference is this won't draw divider for the last item.
 * <p>
 * If in future updates of android support library, this is officially supported, this class
 * will no longer be needed.
 * <p>
 */
public class SkipLastDividerItemDecoration extends DividerItemDecoration {

    private final boolean showLastDivider;  // custom

    public SkipLastDividerItemDecoration(Context context, int orientation, boolean showLastDivider) {
        super(context, orientation);
        this.showLastDivider = showLastDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (showLastDivider) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        int lastItem = parent.getAdapter() == null ? Integer.MAX_VALUE : parent.getAdapter().getItemCount() - 1;
        // hide the divider for the last child
        if (position == lastItem) {
            outRect.setEmpty();
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}