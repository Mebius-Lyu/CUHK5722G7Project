package hk.edu.cuhk.ie.iems5722.a4_1155152375.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.R;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Chatroom;


public class ChatroomAdapter extends ArrayAdapter<Chatroom> {
    private int resourceId;
    public  ChatroomAdapter(Context context,int textViewResourceId,List<Chatroom> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        Chatroom chatroom=getItem(position);//Gets an MSG instance of the current item
        ((TextView) ViewHolderExt.get(convertView, R.id.room_msg)).setText(chatroom.getName());
        return  convertView;
    }
}