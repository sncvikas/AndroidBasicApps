package com.sunincha.appbarsearchsample;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResultsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view to have default list view for the activity
        //Please go thru comment in activity_search_results.xml to know why we are setting content
        //to our own xml even when we are extending ListActivity
        setContentView(R.layout.activity_search_results);

        //handle the intent received from the search menu in the main activity
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        //Handle a new intent
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //Search action starts an activity with android.intent.action.SEARCH
        //Check if it was search intent only
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {

            // search key is passed as intent extra, retrieve the same
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Logic to check if the current list if there is any quote that contains search key
            //if present, show it to the user
            String[] quotes = getResources().getStringArray(R.array.quotes);
            ArrayList<String> quotesWithQueryString = new ArrayList<>();
            for (String q : quotes) {
                if (q.contains(query)) {
                    quotesWithQueryString.add(q);
                }
            }
            if (quotesWithQueryString.size() == 0) {
                Toast.makeText(getBaseContext(), "No quotes found with " + query, Toast.LENGTH_LONG).show();
                return;
            }
            //set the array adapter, by specifying the list layout and passing the quotes araay
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, quotesWithQueryString);
            setListAdapter(adapter);
        }
    }
}
