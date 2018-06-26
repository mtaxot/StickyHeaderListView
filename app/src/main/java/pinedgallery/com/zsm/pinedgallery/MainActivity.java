package pinedgallery.com.zsm.pinedgallery;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zsm.widget.PinedAdapter;
import com.zsm.widget.PinedListViewWrapper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PinedListViewWrapper pl = (PinedListViewWrapper) findViewById(R.id.pined_listview);
        PinedAdapter pinedAdapter = new PinedListViewAdapter(this, prepareTestData());
        pl.getListView().setAdapter(pinedAdapter);
    }


    private List<PinedData> prepareTestData(){
        List<PinedData> list = new ArrayList<>();
        int messageIndex = 0;
        int titleIndex = 0;
        for(int i = 0; i < 10; i++){

            for(int j = 0; j < 3; j++){
                ContentData cd = new ContentData();
                cd.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_NORMAL;
                cd.message = "message#" + messageIndex;
                messageIndex++;
                list.add(cd);
            }
            //prepare 1 title.
            PinedTitleData ptd = new PinedTitleData();
            ptd.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_PINED;
            ptd.title = "Title#" + titleIndex;
            titleIndex++;
            list.add(ptd);

//            ptd = new PinedTitleData();
//            ptd.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_PINED;
//            ptd.title = "Title#" + titleIndex;
//            titleIndex++;
//            list.add(ptd);

            for(int j = 0; j < 3; j++){
                ContentData cd = new ContentData();
                cd.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_NORMAL;
                cd.message = "message#" + messageIndex;
                messageIndex++;
                list.add(cd);
            }

            PinedTitleData ptd1 = new PinedTitleData();
            ptd1.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_PINED_BUTTON;
            ptd1.title = "Button#" + titleIndex;
            titleIndex++;
            list.add(ptd1);
//            ptd1 = new PinedTitleData();
//            ptd1.viewType = PinedListViewAdapter.ITEM_VIEW_TYPE_PINED_BUTTON;
//            ptd1.title = "Button#" + titleIndex;
//            titleIndex++;
//            list.add(ptd1);
        }
        return list;
    }
}

class PinedData{
    public int viewType;
}

class PinedTitleData extends PinedData{
    public String title;
}

class ContentData extends PinedData{
    public String message;
}

class TitleViewHolder{
    public TextView titleTv;
}
class TitleViewHolderButton{
    public TextView titleTv;
    public Button button;
}

class MessageViewHolder{
    public TextView messageTv;
}

class PinedListViewAdapter extends PinedAdapter{
    public static final int ITEM_VIEW_TYPE_PINED_BUTTON = 3;
    public static final int ITEM_VIEW_TYPE_PINED = 2;
    public static final int ITEM_VIEW_TYPE_NORMAL = 1;
    private Context mContext;
    private List<PinedData> mData = new ArrayList<>();
    public PinedListViewAdapter(Context context, List<PinedData> data){
        mContext = context;
        mData.addAll(data);
    }

    public void deleteTitle(int position){

        mData.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public boolean isPined(int position) {
        int pinedType = mData.get(position).viewType;
        if(pinedType == ITEM_VIEW_TYPE_PINED || pinedType == ITEM_VIEW_TYPE_PINED_BUTTON){
            return true;
        }
        return false;
    }
    public int getPinedViewTypeCount(){
        //we have ITEM_VIEW_TYPE_PINED, and ITEM_VIEW_TYPE_PINED_BUTTON , 2 types to be pined.
        return 2;
    }

    @Override
    public View getNormalView(int position, View convertView, ViewGroup parent) {
        /**
         * You must use the convertView.setTag&getTag to optimize.
         */
        MessageViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(
                    mContext).inflate(R.layout.list_item_view_normal, null);
            holder = new MessageViewHolder();
            holder.messageTv = (TextView) convertView.findViewById(R.id.message_tv);
            convertView.setTag(holder);
        }else{
            holder = (MessageViewHolder) convertView.getTag();
        }
        ContentData td = (ContentData)(mData.get(position));
        holder.messageTv.setText(td.message + "");
        return convertView;
    }

    @Override
    public View getPinedView(final int position, View convertView, ViewGroup parent) {
        /**
         * You must use the convertView.setTag&getTag to optimize.
         */
        if(getItemViewType(position) == ITEM_VIEW_TYPE_PINED){
            TitleViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(
                        mContext).inflate(R.layout.list_item_view_pined, null);
                holder = new TitleViewHolder();
                holder.titleTv = (TextView) convertView.findViewById(R.id.pined_title_tv);
                convertView.setTag(holder);
                convertView.setTag(R.id.ok_btn, "small");

            }else{
                holder = (TitleViewHolder) convertView.getTag();
            }
            PinedTitleData td = (PinedTitleData)(mData.get(position));
            holder.titleTv.setText(td.title + "");
        }else if(getItemViewType(position) == ITEM_VIEW_TYPE_PINED_BUTTON){
            TitleViewHolderButton holder;
            if(convertView == null){
                convertView = LayoutInflater.from(
                        mContext).inflate(R.layout.list_item_view_pined_button, null);
                holder = new TitleViewHolderButton();
                holder.titleTv = (TextView) convertView.findViewById(R.id.pined_title_tv);
                holder.button = (Button) convertView.findViewById(R.id.ok_btn);
                convertView.setTag(holder);
                convertView.setTag(R.id.ok_btn, "large");
            }else{
                holder = (TitleViewHolderButton) convertView.getTag();
            }

            PinedTitleData td = (PinedTitleData)(mData.get(position));
            holder.titleTv.setText(td.title + "");
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //deleteTitle(position);
                    Toast.makeText(mContext, "remove", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).viewType;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PinedData getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
