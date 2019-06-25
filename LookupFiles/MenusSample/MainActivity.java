package com.sunincha.menussample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Let's maintain a list of mFruits to show the user
    ArrayList<String> mFruits;

    //Let's maintain another list for archived items
    ArrayList<String> mArchivedFruits;


    //ListView to show the mFruits
    ListView mFruitsList;

    //ListView for archived items
    ListView mArchivedItemsList;

    //TextView for archived fruits title
    TextView mTextViewArchived;


    //ArrayAdapter to tag the list to
    ArrayAdapter<String> mArrayAdapter;
    ArrayAdapter<String> mArchivedListAdapter;

    //Action mode instance
    ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Let's us show popup menu for floating action button
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Snackbar.make(view, menuItem.getTitle()+" is selected",Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        mFruits = new ArrayList<>();
        mFruits.add("Apple");
        mFruits.add("Banana");
        mFruits.add("Orange");
        mFruits.add("Mango");
        mFruits.add("Guava");

        //Assign the view for listview
        mFruitsList = findViewById(R.id.list);

        //Assign the view for listview
        mArchivedItemsList = findViewById(R.id.list_archived);

        mTextViewArchived = findViewById(R.id.archived_fruits_title);

        //Initialise the array adapter with the list items
        mArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mFruits);
        //Tag the adapter to list
        mFruitsList.setAdapter(mArrayAdapter);

        //Lets initialise archived list
        mArchivedFruits = new ArrayList<>();
        //Initialise the array adapter with the list items
        mArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mFruits);
        //Tag the adapter to list
        mFruitsList.setAdapter(mArrayAdapter);


        //Now register context menu for the fruits list
        mArchivedListAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mArchivedFruits);
        mArchivedItemsList.setAdapter(mArchivedListAdapter);
        //Archived list is not shown as it is visibility is set to gone


        //register context menu for fruits list
        registerForContextMenu(mFruitsList);

        //Lets assign an onLongClick listener for archivedItems title
        mTextViewArchived.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mActionMode != null)
                    return false;
                else {

                    //start an action  Mode to show the action menu bar
                    mActionMode = startActionMode(actionModeCallback);
                    view.setSelected(true);
                    return true;
                }
            }
        });


    }


    //This will be called when you open the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i("Menus", "onCreateOptionsMenu");
        return true;
    }


    //This is called everytime menu is opened
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i("Menus", "onPrepareOptionsMenu");
        //Get MenuItem for 'show archived', change the text if its already visible
        MenuItem showArchivedMenu = menu.findItem(R.id.show_archived);
        if (mArchivedItemsList.getVisibility() == View.VISIBLE)
            showArchivedMenu.setTitle("Hide Archived");
        else
            showArchivedMenu.setTitle("Show Archived");

        return true;
    }

    //This will be called when you select an item from menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.show_add:
                //Add an item to the mFruits list
                mFruits.add("Grapes");
                mArrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.show_archived:
                if (mArchivedItemsList.getVisibility() == View.VISIBLE) {
                    mTextViewArchived.setVisibility(View.GONE);
                    mArchivedItemsList.setVisibility(View.GONE);
                } else {
                    mTextViewArchived.setVisibility(View.VISIBLE);
                    mArchivedItemsList.setVisibility(View.VISIBLE);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //This is called when you long click a list item, here set which menu you want to display
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == R.id.list)
            inflater.inflate(R.menu.context_menu, menu);
        //You can show a different context menu by checking the ID of the view
        // if you have called registerForContextMenu for another view as well
    }

    //This is called when an item from the context menu is selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_archive:
                if (!mArchivedFruits.contains(mFruits.get(info.position))) {
                    mArchivedFruits.add(mFruits.get(info.position));
                    mArchivedListAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.context_share:
                shareText(mFruits.get(info.position));
                return true;
            case R.id.context_delete:
                mFruits.remove(info.position);
                mArrayAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (getPackageManager().resolveActivity(intent, 0) != null) {
            startActivity(intent);
        }
    }

    //Define an actionModeCallback for list items to show to action menu on long click
    ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            //inflate menu xml to be shown in action menu
            actionMode.getMenuInflater().inflate(R.menu.action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            //this is called when an item in action menu bar is selected
            //we are deleting all the archived items
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    mArchivedFruits.clear();
                    mArchivedListAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            //when user presses back, we set actinoMode to null
            mActionMode = null;
        }
    };


}
