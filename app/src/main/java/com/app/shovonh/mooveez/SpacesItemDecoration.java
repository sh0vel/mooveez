package com.app.shovonh.mooveez;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Shovon on 6/10/16.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {



        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) % 2 == 0){

            outRect.left = space;
            outRect.right = space/2;

        }else{
            outRect.right = space;
            outRect.left = space/2;
        }

//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
//            outRect.top = space;
//        } else {
//            outRect.top = 0;
//        }
    }
}