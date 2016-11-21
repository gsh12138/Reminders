package com.example.gsh.reminders;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemindersActivity extends ActionBarActivity {
    ListView mListView;
    private RemindersDbAdapter mRemindersDbAdapter;
    private ReminderSimpleCusorAdapter mReminderSimpleCusorAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                Log.d(getLocalClassName(),"Create new Reminder");
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        mListView = (ListView)findViewById(R.id.reminders_list_view);

       mListView.setDivider(null);
        mRemindersDbAdapter=new RemindersDbAdapter(this);
        mRemindersDbAdapter.open();

        mRemindersDbAdapter.createReminder("frist record",true);
        mRemindersDbAdapter.createReminder("second record",false);
        mRemindersDbAdapter.createReminder("third record",false);

        Cursor cursor=mRemindersDbAdapter.fetchAllReminder();

        String[] from = new String[]{RemindersDbAdapter.COL_CONTENT};

        int[] to = new int[]{R.id.row_text};

        mReminderSimpleCusorAdapter=new ReminderSimpleCusorAdapter(
                RemindersActivity.this,
                R.layout.reminders_row,
                cursor,
                from,
                to,
                0
        );

        mListView.setAdapter(mReminderSimpleCusorAdapter);





    }
}
