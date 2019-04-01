package com.snc.quicknotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Custom adapter class that shows the notes retrieved from the database
 * It provides onClickListeners to perform edit and delete operations on the note
 *
 */
public class MyNotesAdapter extends BaseAdapter {

    List<MyNote> mNotes;
    Context mContext;
    LayoutInflater mInflater;
    EditButtonClickListener mEditListener;
    DeleteButtonClickListener mDeleteListener;

    public MyNotesAdapter(Context context, List<MyNote> notes,
                          EditButtonClickListener editListener,
                          DeleteButtonClickListener deleteListener
    ) {
        this.mContext = context;
        this.mNotes = notes;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mEditListener = editListener;
        this.mDeleteListener = deleteListener;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return mNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public String formatDate(String dt) {

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dt);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {
            Utils.showToast(mContext, "Wrong format");
            e.printStackTrace();
        }


        return "";
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.notes_constraint, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextViewTime.setText(formatDate(mNotes.get(i).getTimeStamp()));
        viewHolder.mTextViewNote.setText(mNotes.get(i).getNote());
        viewHolder.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditListener.onEditButtonClicked(i);
            }
        });
        viewHolder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteListener.onDeleteButtonClicked(i);
            }
        });

        return view;
    }
    //We are creating an interface to nofify mainactivity when edit button is clicked on the note
    public interface EditButtonClickListener {
        void onEditButtonClicked(int position);
    }

    //We are creating an interface to nofify mainactivity when delete button is clicked on the note
    public interface DeleteButtonClickListener {
        void onDeleteButtonClicked(int position);
    }

    //Actual view representing each note from the db
    public class ViewHolder {
        TextView mTextViewNote;
        TextView mTextViewTime;
        ImageButton mBtnEdit;
        ImageButton mBtnDelete;

        public ViewHolder(View view) {
            mTextViewNote = view.findViewById(R.id.txt_note_msg);
            mTextViewTime = view.findViewById(R.id.txt_note_time);
            mBtnEdit = view.findViewById(R.id.btn_edit);
            mBtnDelete = view.findViewById(R.id.btn_delete);
        }
    }
}
