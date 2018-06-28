package com.zsm.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import pinedgallery.com.zsm.pinedgallery.R;


/**
 * Created by zsm on 18-6-26.
 */

public class PinedListViewWrapper extends FrameLayout{

    private PinedListView mWrappedListView;

    private int mCurrentPinedPos = -1;

    private volatile View mCurrentPinedView = null;

    private int widthMeasureSpec;

    private int heightMeasureSpec;

    private Map<Integer, View> mCache;


    public PinedListViewWrapper(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PinedListViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PinedListViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mCache = new HashMap<>();
        //construct a listview, so that it fills up the whole wrapper.
        mWrappedListView = new PinedListView(context);
        mWrappedListView.setId(R.id.pined_list_view_id);
        MarginLayoutParams mlp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT,
                MarginLayoutParams.MATCH_PARENT);
        mlp.leftMargin = 0;
        mlp.topMargin = 0;
        mlp.rightMargin = 0;
        mlp.bottomMargin = 0;
        addView(mWrappedListView, mlp);

    }

    public ListView getListView(){
        return mWrappedListView;
    }


    public void onListViewWrapperChildLayout() {
        getCurrentPinedView();
        if(mCurrentPinedView != null){
            View view = mCurrentPinedView;
            /**
             * You should not measure yourself
             * view.measure(widthMeasureSpec, heightMeasureSpec);
             * instead， use measureChildren
             */
            measureChildren(widthMeasureSpec, heightMeasureSpec);

            PinedAdapter adapter = (PinedAdapter) mWrappedListView.getAdapter();
            //update content.
            int firstVisiablePos = mWrappedListView.getFirstVisiblePosition();
            int nextPinedPos = adapter.getNextPinedPosition(firstVisiablePos,
                    mWrappedListView.getLastVisiblePosition());
            int nextPinedIndex = 0 + nextPinedPos - firstVisiablePos;
            int nextPinedViewTop = mWrappedListView.getChildAt(nextPinedIndex).getTop();
            int pinedViewHeight = view.getMeasuredHeight() +  + mWrappedListView.getDividerHeight();
            if(nextPinedViewTop <= pinedViewHeight){
                view.layout(0 + getPaddingLeft() ,
                        nextPinedViewTop - pinedViewHeight + getPaddingTop(),
                        view.getMeasuredWidth() + getPaddingLeft(),
                        nextPinedViewTop + getPaddingTop());
            }else{
                view.layout(0 + getPaddingLeft(),
                        0 + getPaddingTop(),
                        view.getMeasuredWidth() + getPaddingLeft(),
                        view.getMeasuredHeight() + getPaddingTop());
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec)
                        - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY);

        this.heightMeasureSpec = MeasureSpec.makeMeasureSpec
                (MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST);
    }

    /**
     * 寻找当前应该pin的view,
     * @return
     */
    private void getCurrentPinedView(){

        PinedAdapter adapter = (PinedAdapter) mWrappedListView.getAdapter();
        int pinedPos = adapter.getPinedPosition(mWrappedListView.getFirstVisiblePosition());
        if(pinedPos < 0){
            if(mCurrentPinedView != null && mCurrentPinedView.getVisibility() == View.VISIBLE){
                mCurrentPinedView.setVisibility(View.GONE);
                mCurrentPinedPos = -1;
                mCurrentPinedView = null;
            }
        }else{
            if(pinedPos != mCurrentPinedPos){

                //handle cache
                int pinedType = adapter.getItemViewType(pinedPos);

                View cachedPinedView = mCache.get(pinedType);
                if(cachedPinedView == null){
                    cachedPinedView = adapter.getPinedView(pinedPos, null, this);
                    mCache.put(pinedType, cachedPinedView);
                    MarginLayoutParams mlp = createDefaultPinedViewLayoutParam();
                    cachedPinedView.setLayoutParams(mlp);
                    addView(cachedPinedView);
                }else{
                    View v = adapter.getPinedView(pinedPos, cachedPinedView, this);
                    if(cachedPinedView != v){
                        /**
                         * if you don't use convertView.setTag to optimize,
                         * just we give you a warning, but you lose the performance
                         * you see, the following code?? if you don't they will be executed times.
                         */
                        Log.w(PinedListViewWrapper.class.getSimpleName(),
                                "if you don't use convertView.setTag to optimize, you lose the performance");
                        mCache.put(pinedType, v);
                        removeView(cachedPinedView);
                        MarginLayoutParams mlp = createDefaultPinedViewLayoutParam();
                        v.setLayoutParams(mlp);
                        addView(v);

                    }
                    cachedPinedView = v;
                }
                if(mCurrentPinedView != null && mCurrentPinedView.getVisibility() == View.VISIBLE){
                    mCurrentPinedView.setVisibility(View.GONE);
                }

                mCurrentPinedPos = pinedPos;
                mCurrentPinedView = cachedPinedView;
                mCurrentPinedView.setVisibility(View.VISIBLE);
            }

        }
        //Log.e("zsm", "show : " + " " + mCurrentPinedPos);

    }

    private MarginLayoutParams createDefaultPinedViewLayoutParam(){
        MarginLayoutParams mlp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT,
                MarginLayoutParams.MATCH_PARENT);
        mlp.leftMargin = 0;
        mlp.topMargin = 0;
        mlp.rightMargin = 0;
        mlp.bottomMargin = 0;
        return mlp;
    }



}
