package hk.edu.cuhk.ie.iems5722.a4_1155152375.Adapter;

import android.util.SparseArray;
import android.view.View;

public  class  ViewHolderExt{
    @SuppressWarnings("unchecked")
    public static  <T extends View> T get(View view, int id){
        SparseArray<View> viewHolder=(SparseArray<View>)view.getTag();
        if(viewHolder==null)
        {
            viewHolder=new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView=viewHolder.get(id);
        if(childView==null){
            childView=view.findViewById(id);
            viewHolder.put(id,childView);
        }
        return  (T)childView;
    }
}
