package com.example.gsh.reminders;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/21.
 */

public class RemindersDbAdapter {
    public static final String COL_ID="_id";
    public static final String COL_CONTENT="_content";
    public static final String COL_IMPORTANT="_important";
    public static final int INDEX_ID=0;
    public static final int INDEX_CONTENT=INDEX_ID+1;
    public static final int INDEX_IMPORTANT=INDEX_ID+1;

    private static final String TAG="RemindersDbAdapter";
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME="dba_remdrs";
    private static final String TABLE_NAME="tbl_remdrs";
    private static final int DATABASE_VERSION=1;

    private  Context mCtx;

    private static final String DATABASE_CREATE="CREATE TABLE if not exists "+TABLE_NAME+" ( "+
                                                              COL_ID+" INTEGER PRIMARY KEY autoincrement, "+
                                                              COL_CONTENT+" TEXT, "+
                                                              COL_IMPORTANT+" INTEGER );";

    public RemindersDbAdapter(Context ctx) {
        mCtx = ctx;
    }

    public void open() throws SQLException{
        mDatabaseHelper=new DatabaseHelper(mCtx);
        mDb=mDatabaseHelper.getWritableDatabase();
    }

    public void close() throws SQLException{
        if(mDatabaseHelper!=null){
            mDatabaseHelper.close();
        }
    }

    public void createReminder(String name,boolean important){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,name);
        values.put(COL_IMPORTANT,important?1:0);
        mDb.insert(TABLE_NAME,null,values);
    }

    public long createReminder(Reminder reminder){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,reminder.getContent());
        values.put(COL_IMPORTANT,reminder.getImportant());
        return mDb.insert(TABLE_NAME,null,values);
    }

    public Reminder fetchReminderById(int id){
        Cursor cursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},COL_ID+" =?",
                                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor !=null){
            cursor.moveToFirst();
        }
        return  new Reminder(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IMPORTANT)

        );
    }

    public Cursor fetchAllReminder(){
        Cursor cursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},null,null,null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void updateReminder(Reminder reminder){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,reminder.getContent());
        values.put(COL_IMPORTANT,reminder.getImportant());
        mDb.update(TABLE_NAME,values,COL_ID+" =?",new String[]{String.valueOf(reminder.getMid())});
    }

    public void deleteReminderById(int id){
        mDb.delete(TABLE_NAME,COL_ID+" =?",new String[]{String.valueOf(id)});
    }

    public void deleteAllReminders(){
        mDb.delete(TABLE_NAME,null,null);
    }
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.w(TAG,DATABASE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG,"Upgrading datbase from version "+i+" to "+i1+", which will destroy all old data");
            mDb.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
