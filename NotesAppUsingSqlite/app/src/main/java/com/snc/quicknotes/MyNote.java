package com.snc.quicknotes;

/**
 * Class that represents the NOTE object with its own characteristics
 * Each note has
 * 1. unique id used in db storage
 * 2. note, actual note text
 * 3. time, time the note was created
 *
 */

public class MyNote {
    public static final String TAG = "MyNote";
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIME = "timestamp";

    //raw sql statement to create a new table named notes
    // column 'id' is a integer primary key
    // column 'note' is text representing the actual text
    // column 'time' has the time the note is created at, it default to current timestamp if no
    //value is passed
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTE + " TEXT NOT NULL,"
            + COLUMN_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP )";
    private int mId;
    private String mNote;
    private String mTimeStamp;
    ;

    public MyNote() {
    }

    public MyNote(int id, String note, String timestamp) {
        this.mId = id;
        this.mNote = note;
        this.mTimeStamp = timestamp;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getNote() {
        return this.mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }

    public String getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(String timestamp) {
        this.mTimeStamp = timestamp;
    }
}
