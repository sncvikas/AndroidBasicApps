package com.snc.quicknotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as an interface between the view(UI) and the database
 * Provides apis to perform db operations such as query, insert, update and delete
 *
 * Observe that in each method, we are opening db before we either write to or read from it
 * and we close the db after we are done using it.
 *
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "notes.db";
    public static final int DB_VERSION = 1;
    private static final String TAG = "MyDatabaseHelper";
    Context mContext;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    //This onCreate is called first time when the db is created
    //i.e when the app is launched the first time

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We are running raw sql statement to create notes table
        sqLiteDatabase.execSQL(MyNote.CREATE_TABLE);
    }

    //This onUpgrate is called when the app upgraded with change is DB_VERSION value
    //Currently it is 1
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //if a new version is present we delete old table and create new one
        if (i1 > 1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyNote.TABLE_NAME + " ;");
        }
        onCreate(sqLiteDatabase);
    }

    //All below methods represents corresponding db operations
    // such as insert, update, query, delete


    //insert api requires a ContentValues object which contains all column values
    // that dont have a default
    public long insertNote(ContentValues values) {
        long id;
        SQLiteDatabase db = this.getWritableDatabase();
        id = db.insertOrThrow(MyNote.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //update is similar to insert except that it needs where clause to identify which row to update
    //based on the column value
    public long updateNote(long id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String whereClause = MyNote.COLUMN_ID + " = ?";
        final String whereArgs[] = new String[]{String.valueOf(id)};
        long id_return = db.update(MyNote.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        return id_return;
    }

    //delete api deletes a note from the db, based on the where clause
    public long deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String whereClause = MyNote.COLUMN_ID + " = ?";
        final String whereArgs[] = new String[]{String.valueOf(id)};
        long id_return = db.delete(MyNote.TABLE_NAME, whereClause, whereArgs);
        db.close();
        return id_return;
    }

    //query method retrieves rows from the database
    public List<MyNote> getAllNotes() {
        List<MyNote> currentNotes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MyNote.TABLE_NAME, null, null, null,
                null, null, MyNote.COLUMN_TIME + " DESC");

        if (cursor.getCount() == 0) {
            ;
        } else {
            cursor.moveToFirst();
            do{
                currentNotes.add(new MyNote(cursor.getInt(cursor.getColumnIndex(MyNote.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(MyNote.COLUMN_NOTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MyNote.COLUMN_TIME))));
            }while (cursor.moveToNext());
        }
        db.close();
        return currentNotes;
    }

    //here were are just querying one note from the db based on the input id
    public MyNote getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        MyNote note = null;

        //projection is the columns that are returned from the query
        final String projection[] = new String[]{MyNote.COLUMN_ID, MyNote.COLUMN_NOTE, MyNote.COLUMN_TIME};

        // where clause defines the condition to be met for the row to be returned
        // we pass only the column name to match and the oprator
        final String whereClause = MyNote.COLUMN_ID + " =?";

        //here we pass the values to be matched against the column name mentioed in the above
        //whereclause
        final String whereArgs[] = new String[]{String.valueOf(id)};

        //query method return Cursor object which contains the values of each row again each column
        Cursor cursor = db.query(MyNote.TABLE_NAME, projection, whereClause, whereArgs, null,
                null, null);

        if (cursor.getCount() == 0) {
            Utils.infoLog(TAG, "Empty list");
        } else {
            cursor.moveToFirst();

            Utils.infoLog(TAG, "while getNote");
            note = new MyNote(cursor.getInt(cursor.getColumnIndex(MyNote.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(MyNote.COLUMN_NOTE)),
                    cursor.getString(cursor.getColumnIndex(MyNote.COLUMN_TIME)));
        }

        db.close();
        return note;
    }
}