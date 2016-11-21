package com.example.gsh.reminders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemindersActivity extends AppCompatActivity {
    ListView mListView;

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

        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,R.layout.reminders_row,R.id.row_text,
                                                                      new String[]{"first record","second record","third record"});

        mListView.setAdapter(mArrayAdapter);





    }
}
