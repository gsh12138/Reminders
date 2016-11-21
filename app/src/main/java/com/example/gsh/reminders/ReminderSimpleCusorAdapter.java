package com.example.gsh.reminders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ReminderSimpleCusorAdapter extends SimpleCursorAdapter {
    public ReminderSimpleCusorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder==null){
            holder=new ViewHolder();
            holder.colTmp=cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTANT);
            holder.listTab=view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }
        if (cursor.getInt(holder.colTmp)>0){
            holder.listTab.setBackground(context.getResources().getDrawable(R.color.orange));

        }else {
            holder.listTab.setBackground(context.getResources().getDrawable(R.color.green));
        }
    }

    private class ViewHolder {
        public int colTmp;
        public View listTab;
    }
}
