package com.apress.gerber.reminders;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
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

public class RemindersActivity extends ActionBarActivity {
    private ListView mListView;
private RemindersDbAdapter mDbAdapter;
      private RemindersSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true)
        ;

        actionBar.setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.activity_reminders);
        mListView=(ListView)findViewById(R.id.reminders_list_view);
//        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.reminders_row,R.id.row_text,new String[]{"first record","second record","third record"});
//        mListView.setAdapter(arrayAdapter);

        mListView.setDivider(null);
        mDbAdapter=new RemindersDbAdapter(this);
        mDbAdapter.open();
        if(savedInstanceState==null){
            //clear all data
            mDbAdapter.deleteAllReminders();
            //add some data
            insertSomeReminders("Buy Learn android studio", true);
            insertSomeReminders("Send Dad birthday gift", false);
            insertSomeReminders("dinner at the gage on friday", false);
            insertSomeReminders("string sqush racket", false);
            insertSomeReminders("shovel and salt walkways", false);
            insertSomeReminders("Prepare advanced android syllabus", true);
            insertSomeReminders("Buy new office chair", false);
            insertSomeReminders("Call Auto-body shop for quote", false);
            insertSomeReminders("Renew membership to  club", false);
            insertSomeReminders("Buy new Galaxy Android phone", true);
            insertSomeReminders("Sell old Android phone ", true);
            insertSomeReminders("Buy old android phone - auction", false);
            insertSomeReminders("Call accoutant about tax returns", false);
            insertSomeReminders("Buy 300000 shres of google", false);
            insertSomeReminders("call the dalai Lama back", true);
        }
        //removeed
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int masterListPosition, long id) {
              //  Toast.makeText(RemindersActivity.this, "clicked "+position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(RemindersActivity.this);
                ListView modeListView=new ListView(RemindersActivity.this);
                String[] modes=new String[]{"edit reminder","delete reminder"};
                ArrayAdapter<String> modeAdapter=new ArrayAdapter<String>(RemindersActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog=builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                        //edit reminder
                        if (position==0){
                          //  Toast.makeText(RemindersActivity.this,"edit"+masterListPosition,Toast.LENGTH_SHORT).show();
                            int nid=getIdFromPosition(masterListPosition);
                            Reminder reminder=mDbAdapter.fetchReminderById(nid);
                            fireCustomDialog(reminder);

                            //delete reminder
                        }else{
                            //Toast.makeText(RemindersActivity.this, "delete"+masterListPosition, Toast.LENGTH_SHORT).show();
                        mDbAdapter.deleteReminderByid(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
                @Override
                public  void onItemCheckedStateChanged(ActionMode mode, int position,long id,boolean checked){

                }
                @Override
                public  boolean onCreateActionMode(ActionMode mode,Menu menu){
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu,menu);
                    return true;
                }
                @Override
                public  boolean onPrepareActionMode(ActionMode mode,Menu menu){
                    return false;
                }
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onActionItemClicked(ActionMode mode,MenuItem item){
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_reminder:
                            for (   int nC=mCursorAdapter.getCount()-1;nC>=0;nC--){
                            if(mListView.isItemChecked(nC)){
                                mDbAdapter.deleteReminderByid(getIdFromPosition(nC));
                            }
                        }
                        mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return  true;
                    }
                    return false;
                }



                @Override
                public void onDestroyActionMode(ActionMode mode){}
            });
        }
    }

    private int getIdFromPosition(int nC) {
        return (int)mCursorAdapter.getItemId(nC);
    }
    private void insertSomeReminders(String name, boolean important) {
        mDbAdapter.createReminder(name, important);
    }
private void fireCustomDialog(final Reminder reminder){
    //custom dialog
    final  Dialog dialog=new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_custom);
    TextView titleView=(TextView)dialog.findViewById(R.id.custom_title);
    final EditText editCustom=(EditText) dialog.findViewById(R.id.custom_edit_reminder);
    Button commitButton=(Button) dialog.findViewById(R.id.custom_button_commit);
    Button buttonCancel=(Button)dialog.findViewById(R.id.custom_button_cancel);
    final CheckBox checkBox=(CheckBox)dialog.findViewById(R.id.custom_check_box);
    LinearLayout rootLayout=(LinearLayout)dialog.findViewById(R.id.custom_root_layout);
    final boolean isEditOperation=(reminder!=null);
    //this is for edit
    if(isEditOperation) {
        titleView.setText("Edit Reminder");
        checkBox.setChecked(reminder.getImportant() == 1);
        editCustom.setText(reminder.getImportant());
        rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    commitButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String reminderText=editCustom.getText().toString();
            if(isEditOperation){
                Reminder remindEdited=new Reminder(reminder.getId(),reminderText,checkBox.isChecked()?1:0);
                mDbAdapter.updateReminder(remindEdited);
                //this is for new reminder
            }
            else{
                mDbAdapter.createReminder(reminderText,checkBox.isChecked());
            }
            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
            dialog.dismiss();
        }
    });
    buttonCancel.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            dialog.dismiss();
        }
    });
    dialog.show();
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.action_new:
                //Log.d(getLocalClassName(),"create new Reminder");
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:finish();return true;
            default:return  false;

        }

    }
}
