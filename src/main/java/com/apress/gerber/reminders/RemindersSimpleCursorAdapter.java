package com.apress.gerber.reminders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 2018-07-31.
 */

public class RemindersSimpleCursorAdapter extends SimpleCursorAdapter{
    public RemindersSimpleCursorAdapter(Context context, int layout, Cursor c,String[] from,int[] to,int flags){
        super(context,layout,c,from,to,flags);
    }
    //to use  a viewholder,you must override the flollowing two methods and define a ViewHolder class
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return  super.newView(context,cursor,parent);

    }
    @Override
    public void bindView(View view,Context context,Cursor cursor)
    {
        super.bindView(view,context,cursor);
        ViewHolder holder=(ViewHolder) view.getTag();
        if(holder==null){
            holder=new ViewHolder();
            holder.collmp=cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTANT);
            holder.listTab=view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }
        if(cursor.getInt(holder.collmp)>0){
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }else{
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }
    static class ViewHolder{
        //store the column indel
        int collmp;
        //store the view
        View listTab;
    }
}
