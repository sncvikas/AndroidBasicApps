package com.snc.quicknotes;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    EditText mEditNewNote;
    ImageButton mBtnAddNote;
    ListView mListNotes;
    MyNotesAdapter mMyNotesAdapter;
    List<MyNote> mMyCurrentNotes;
    MyDatabaseHelper mDbHelper;

    ConstraintLayout mConstraintLayout;
    MyNotesAdapter.EditButtonClickListener mEditListener;
    MyNotesAdapter.DeleteButtonClickListener mDeleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialising all the ui widgets here
        mEditNewNote = findViewById(R.id.edit_new_note);
        mBtnAddNote = findViewById(R.id.btn_add);
        mBtnAddNote.setOnClickListener(this);
        mConstraintLayout = findViewById(R.id.main_constraint_layout);

        //Initialising an object of a helper class that communicates with the database
        mDbHelper = new MyDatabaseHelper(getApplicationContext());

        mMyCurrentNotes = new ArrayList<>();

        mListNotes = findViewById(R.id.list_notes);

        mEditListener = new MyNotesAdapter.EditButtonClickListener() {
            @Override
            public void onEditButtonClicked(int position) {
                showNotesDialog(position);
            }
        };

        mDeleteListener = new MyNotesAdapter.DeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClicked(int position) {
                MyNote note = mMyCurrentNotes.get(position);
                deleteNote(note.getId(), position);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        //We load notes from database everytime application is loaded
        // this is called even when app is relaunched from recent app
        //Please learn more about activity life cycle to understand it better
        new GetNotesFirstTime().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Quick Notes");
            try {
                builder.setMessage("Version " + getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A dialog that shows the selected note to further edit and update.
     * @param position of the note
     */
    public void showNotesDialog(final int position) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.create_note_layout, null);
        dialog.setView(view);
        TextView txtViewTile = view.findViewById(R.id.txt_title);
        final EditText editNote = view.findViewById(R.id.edit_message);
        final String title = "Edit Note";
        final String msg = mMyCurrentNotes.get(position).getNote();

        txtViewTile.setText(title);
        editNote.setText(msg);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String message = editNote.getText().toString().trim();
                if (message == null || message.isEmpty()) {
                    Utils.makeASnackbar(mConstraintLayout, "Please enter a note");
                    return;
                }
                dialogInterface.dismiss();
                updateNote(mMyCurrentNotes.get(position).getId(), message, position);

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        dialog.create().show();
    }

    public void addNote(String note) {
        ContentValues values = new ContentValues();
        values.put(MyNote.COLUMN_NOTE, note);
        long id = mDbHelper.insertNote(values);
        MyNote myNote = mDbHelper.getNote(id);
        if (myNote != null) {
            Utils.makeASnackbar(mConstraintLayout, "Added successfully");
            mMyCurrentNotes.add(0, myNote);
            mMyNotesAdapter.notifyDataSetChanged();
        }
        mEditNewNote.setText("");
        mEditNewNote.clearFocus();
    }

    public void updateNote(long id, String note, int position) {
        ContentValues values = new ContentValues();
        values.put(MyNote.COLUMN_NOTE, note);
        long id_updated = mDbHelper.updateNote(id, values);
        MyNote myNote = mDbHelper.getNote(id);
        if (myNote != null) {
            Utils.makeASnackbar(mConstraintLayout, "Updated successfully");
            Utils.infoLog(TAG, "UpdatedID " + myNote.getId() + "===" + String.valueOf(id_updated));
            mMyCurrentNotes.set(position, myNote);
            mMyNotesAdapter.notifyDataSetChanged();
        }
    }

    public void deleteNote(long id, int position) {
        mDbHelper.deleteNote(id);
        mMyCurrentNotes.remove(position);
        mMyNotesAdapter.notifyDataSetChanged();
        Utils.makeASnackbar(mConstraintLayout, "Deleted successfully");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String message = mEditNewNote.getText().toString().trim();
                if (message == null || message.isEmpty()) {
                    Utils.makeASnackbar(mConstraintLayout, "Please enter a note");
                    return;
                }
                addNote(message);
                break;
        }
    }

    /**
     * This asynctask loads the list of current notes from database when the application is started
     */
    class GetNotesFirstTime extends AsyncTask<Void, Void, Integer> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading notes");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            //Here we query all the notes from the database
            mMyCurrentNotes = mDbHelper.getAllNotes();
            return mMyCurrentNotes.size();
        }

        @Override
        protected void onPostExecute(Integer numberOfNotes) {
            super.onPostExecute(numberOfNotes);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (mMyCurrentNotes.size() == 0) {
                Utils.makeASnackbar(mConstraintLayout, "You have no notes!!");

            }
            //After we are done querying the database for the notes, we show them in the list.
            mMyNotesAdapter = new MyNotesAdapter(getBaseContext(), mMyCurrentNotes, mEditListener,
                    mDeleteListener);
            mListNotes.setAdapter(mMyNotesAdapter);

        }
    }
}
