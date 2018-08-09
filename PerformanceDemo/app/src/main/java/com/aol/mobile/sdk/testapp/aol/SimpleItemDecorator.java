package com.aol.mobile.sdk.testapp.aol;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class SimpleItemDecorator extends RecyclerView.ItemDecoration {
    private final int space;

    public SimpleItemDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = space;
        outRect.right = space;
        outRect.left = space;
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = space;
        else
            outRect.top = 0;
    }
}
