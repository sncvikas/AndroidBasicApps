package com.sunincha.webapiusage;

import android.content.Context;
import android.icu.util.UniversalTimeScale;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieTitle> {

    public static final String TAG = MainActivity.class.getName();

    //Here is webapi to get details of the movie by its name
    public static final String URL = "http://www.omdbapi.com/?t=";
    String mSearchMovie;

    //Lets create member variables for views
    EditText mEditSearch;
    Button mBtnSearch;

    TextView mTxtName;
    TextView mRating;
    TextView mGenre;
    TextView mYearOfRelease;
    TextView mLanguage;
    TextView mSearchResultTitle;

    //To hide or unhide based on search result
    LinearLayout mLlResult;

    //Progress bar to show while fetching the data
    ProgressBar mProgressBar;

    LoaderManager loaderManager;

    ConnectivityManager cm;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise all the required views
        mEditSearch = findViewById(R.id.edit_name);
        mBtnSearch = findViewById(R.id.btn_search);
        mLlResult = findViewById(R.id.ll_result);

        //TextViews to show fetched data
        mTxtName = findViewById(R.id.txt_mv_name);
        mLanguage = findViewById(R.id.txt_mv_language);
        mRating = findViewById(R.id.txt_mv_rating);
        mGenre = findViewById(R.id.txt_mv_genre);
        mYearOfRelease = findViewById(R.id.txt_mv_year);

        //Title of the result
        mSearchResultTitle = findViewById(R.id.txt_result_title);

        //Progress bar
        mProgressBar = findViewById(R.id.progress_circular);
        //Hide the progress when activity is started
        mProgressBar.setVisibility(View.GONE);

        //get connection manager instance to check if internet is available
        cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        mLlResult.setVisibility(View.GONE);
        loaderManager = getSupportLoaderManager();
        //Set an onclicklistener for button to fetch data for input movie
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.debugLog(TAG, "onClick");
                if (!isConnected) {
                    Toast.makeText(getBaseContext(), "Please connect to internet to search for movies", Toast.LENGTH_LONG).show();
                    return;
                }
                mSearchMovie = mEditSearch.getText().toString().trim();
                if (TextUtils.isEmpty(mSearchMovie)) {
                    Toast.makeText(getBaseContext(), "Please enter search title", Toast.LENGTH_SHORT).show();
                    return;
                }
                //When we are starting the fetch the data from server
                //Hide linearlayout that had earlier result if any
                //show the tile as searching
                //show progress bar
                mLlResult.setVisibility(View.GONE);
                mSearchResultTitle.setVisibility(View.VISIBLE);
                mSearchResultTitle.setText("Searching...");
                mProgressBar.setVisibility(View.VISIBLE);

                // initialise an asynctask loader
                //first argument is the id for the loader, used if app has more than one loader
                //second argument is input data for loader, if any
                //third is callback handler for loader callbacks

                Loader<MovieTitle> loader = loaderManager.getLoader(1);
                if (loader == null) {
                    loaderManager.initLoader(1, null, MainActivity.this);
                } else {
                    loaderManager.restartLoader(1, null, MainActivity.this);
                }
            }
        });

        mSearchResultTitle.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check is internet is available
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    @NonNull
    @Override
    public Loader<MovieTitle> onCreateLoader(int i, @Nullable Bundle bundle) {
        //THis is called when we initialise the loader when user click search

        //creating a final url to fetch data from
        //api key is specific key generatated on imdb website to authenticate the request
        String finalUrl = URL + mSearchMovie + "&apikey=1c23584d";
        Utils.debugLog(TAG, "URL is " + finalUrl);

        //create an instance of the loader
        //which internally calls onStartLoading method of the MovieLoader class
        return new MovieLoader(getApplicationContext(), finalUrl);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieTitle> loader, MovieTitle movieTitle) {
        //called when the loader finished the fetch operation

        if (movieTitle == null) {
            Toast.makeText(getApplicationContext(), "Movie not found", Toast.LENGTH_LONG).show();
            mSearchResultTitle.setText("No movie found with that name");
            mSearchResultTitle.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        //When data is fetched from the server
        //Hide the progress bar
        mProgressBar.setVisibility(View.GONE);

        // set each textview with its corresponding data
        mTxtName.setText(movieTitle.getTile());
        mGenre.setText(movieTitle.getGenre());
        mRating.setText(String.valueOf(movieTitle.getRating()));
        mYearOfRelease.setText(String.valueOf(movieTitle.getYearOfRelease()));
        mLanguage.setText(movieTitle.getLanguage());

        //show the linearlyout
        mLlResult.setVisibility(View.VISIBLE);

        //Change the title to result
        mSearchResultTitle.setText("Movie Details");
        mSearchResultTitle.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieTitle> loader) {

    }

}
