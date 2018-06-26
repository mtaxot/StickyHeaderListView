package com.zsm.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by zsm on 18-6-25.
 */

public abstract class PinedAdapter extends BaseAdapter{


    /**
     * If the position with its view needs to be pined, then just turn true.
     * This is what you have to tell the PinedListViewWrapper in order to pined this position.
     */
    public abstract boolean isPined(int position);

    /**
     * Normal item like getView(int position, View convertView, ViewGroup parent).
     * You should implement this method associated with getPinedView().
     * PinedListViewWrapper assumed that you use convertView.setTag() to optimize the performance,
     * or you won't get properly pined behavior.
     */
    public abstract View getNormalView(int position, View convertView, ViewGroup parent);
    public abstract View getPinedView(int position, View convertView, ViewGroup parent);


    public int getNormalViewTypeCount(){
        return 1;
    }
    public int getPinedViewTypeCount(){
        return 1;
    }


    /**
     * Given the first visable position in listview, return the current pined position.
     * Pined Position means isPined() return true.
     * isPined() is also another method that you should implement.
     */
    public int getPinedPosition(int firstvisiablePos) {
        for(int i = firstvisiablePos; i >=0; i--){
            if(isPined(i)){
                return i;
            }
        }
        return -1;
    }


    public int getNextPinedPosition(int currentPinedPos, int lastvisiablePos) {
        for(int i = currentPinedPos + 1; i <= lastvisiablePos; i++){
            if(isPined(i)){
                return i;
            }
        }
        return -1;
    }


    @Override
    public int getViewTypeCount() {
        return getNormalViewTypeCount() + getPinedViewTypeCount() + 1;
    }




    /**
     * subclass is not required to implement this method,
     * instead, override getNormalView & getPinedView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(isPined(position)){
            convertView = getPinedView(position, convertView, parent);
        }else{
            convertView = getNormalView(position, convertView, parent);
        }

        return convertView;
    }


}
