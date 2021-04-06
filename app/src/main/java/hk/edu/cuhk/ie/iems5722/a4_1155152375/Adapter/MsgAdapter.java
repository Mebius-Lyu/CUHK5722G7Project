package hk.edu.cuhk.ie.iems5722.a4_1155152375.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.R;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Msg;


public class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceId;
    public  MsgAdapter(Context context,int textViewResourceId,List<Msg> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        Msg msg=getItem(position);//Gets an MSG instance of the current item
        if (msg.getType()==1)
        {
            ViewHolderExt.get(convertView, R.id.left_layout).setVisibility(View.VISIBLE);
            ViewHolderExt.get(convertView,R.id.right_layout).setVisibility(View.GONE);
            ((TextView)ViewHolderExt.get(convertView,R.id.left_msg)).setText(msg.getContent());
        }
        else if(msg.getType()==0)
        {
            ViewHolderExt.get(convertView,R.id.right_layout).setVisibility(View.VISIBLE);
            ViewHolderExt.get(convertView,R.id.left_layout).setVisibility(View.GONE);
            ((TextView)ViewHolderExt.get(convertView,R.id.right_msg)).setText(msg.getContent());
        }
        return convertView;
    }
}