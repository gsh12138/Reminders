package com.example.gsh.reminders;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RemindersActivity extends ActionBarActivity {
    ListView mListView;
    private RemindersDbAdapter mRemindersDbAdapter;
    private ReminderSimpleCusorAdapter mReminderSimpleCusorAdapter;

    private void fireCustomDialog(final  Reminder reminder){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        TextView titleView=(TextView)dialog.findViewById(R.id.custom_title);
        final EditText editCustom=(EditText)dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button)dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox)dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout=(LinearLayout)dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (reminder!=null);
        if(isEditOperation){
            titleView.setText("Edit Reminder");
            checkBox.setChecked(reminder.getImportant()==1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));

        }

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reminderText=editCustom.getText().toString();
                if(isEditOperation){
                    Reminder reminderEdited = new Reminder(reminder.getMid(),reminderText,checkBox.isChecked()?1:0);
                    mRemindersDbAdapter.updateReminder(reminderEdited);

                }else {
                    mRemindersDbAdapter.createReminder(reminderText,checkBox.isChecked());

                }
                mReminderSimpleCusorAdapter.changeCursor(mRemindersDbAdapter.fetchAllReminder());
                dialog.dismiss();
            }
        });
        Button buttonCancel=(Button)dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                fireCustomDialog(null);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int masterListPositon, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(RemindersActivity.this);
                ListView modeListView = new ListView(RemindersActivity.this);
                String[] modes = new String[]{"Edit Reminder","Delet Reminder"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(RemindersActivity.this,
                                        android.R.layout.simple_list_item_1,android.R.id.text1,modes);
                modeListView.setAdapter(modeAdapter);

                builder.setView(modeListView);
                final Dialog dialog=builder.create();
                dialog.show();;
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i==0){
                            int nId=(int)mReminderSimpleCusorAdapter.getItemId(masterListPositon);
                            Reminder reminder = mRemindersDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(reminder);
                        }else {
                            mRemindersDbAdapter.deleteReminderById((int)mReminderSimpleCusorAdapter.getItemId(masterListPositon));
                            mReminderSimpleCusorAdapter.changeCursor(mRemindersDbAdapter.fetchAllReminder());
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater inflater=actionMode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_item_delete_reminder:
                            for (int nC=mReminderSimpleCusorAdapter.getCount()-1;nC>=0;nC--){
                                if(mListView.isItemChecked(nC)){
                                    mRemindersDbAdapter.deleteReminderById((int)mReminderSimpleCusorAdapter.getItem(nC));
                                }
                            }
                            actionMode.finish();
                            mReminderSimpleCusorAdapter.changeCursor(mRemindersDbAdapter.fetchAllReminder());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }





    }
}
