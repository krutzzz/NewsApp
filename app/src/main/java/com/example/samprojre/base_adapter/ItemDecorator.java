package com.example.samprojre.base_adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorator extends RecyclerView.ItemDecoration {

    private final int topMargin;
    private final int leftAndRightMargin;
    private final int bottomMargin;


    public ItemDecorator(@IntRange(from = 0) int topMargin,
                         @IntRange(from = 0) int bottomMargin,
                         @IntRange(from = 0) int leftAndRightMargin) {
        this.topMargin = topMargin;
        this.leftAndRightMargin = leftAndRightMargin;
        this.bottomMargin = bottomMargin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               RecyclerView parent, @NonNull RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        if(position==0){
            outRect.top = topMargin;
        }
        outRect.right = leftAndRightMargin;
        outRect.left = leftAndRightMargin;
        outRect.bottom = bottomMargin;

    }
}
