package com.zsm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class PinedListView extends ListView{




    public PinedListView(Context context) {
        super(context);
    }

    public PinedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * here we notify the wrapper to update its layout.
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean res =  super.drawChild(canvas, child, drawingTime);
        ((PinedListViewWrapper) getParent()).onListViewWrapperChildLayout();
        return res;
    }

}
