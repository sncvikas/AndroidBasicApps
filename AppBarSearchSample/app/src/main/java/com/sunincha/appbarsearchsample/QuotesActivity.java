package com.sunincha.appbarsearchsample;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class QuotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        //Create an instance of the appbar declared in xml
        Toolbar toolbar = findViewById(R.id.toolbar);
        //show the app inplace of action bar
        setSupportActionBar(toolbar);



        //Create an instance of floating action button
        FloatingActionButton fab = findViewById(R.id.fab);

        //Set an onclick listener, Floating action button does not have any action as of now
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No action provided", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu from /res/menu/menu.xml
        getMenuInflater().inflate(R.menu.menu, menu);

        //Assign a search for one of the menu items
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // assign search for id.search menu item
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                //share
                //As an example, lets share the first quote
                shareFirstQuote();
                break;
            case R.id.settings:
                //settings
                // does not have any action
                Toast.makeText(this, "Settings is tapped", Toast.LENGTH_LONG).show();
                break;
            default:
                //ensure to do default action provided by system
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void shareFirstQuote() {
        //Intent filter to start send intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        //Assign text/plain as the mimetype for the intent to be sent
        intent.setType("text/plain");
        //Put text to be shared in an extra, here we are always sharing the first quote from the list
        intent.putExtra(Intent.EXTRA_TEXT,getResources().getStringArray(R.array.quotes)[0]);

        //Check if android has any app to handle share intent
        if(intent.resolveActivity(getPackageManager()) != null ){
            //start an activity to share the text,
            startActivity(intent);
        }else {
            //Just show a toast saying share menu is clicked
            Toast.makeText(getBaseContext(), "Share menu is tapped", Toast.LENGTH_SHORT).show();
        }
    }
}
