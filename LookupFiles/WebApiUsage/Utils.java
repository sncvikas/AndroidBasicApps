package com.sunincha.webapiusage;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class Utils {
    public static final String TAG = "Utils";
    public static final String APP_LOG_TAG = "FetchMovie";

    private static URL createUrl(String stringUrl) {
        debugLog(TAG, "createUrl");

        //check if the passed string is a valid URL
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Url is invalid");
        }
        return url;
    }

    private static String getMovieData(URL url) {
        debugLog(TAG, "getMovieData");
        if (url == null) {
            return null;
        }

        String responseString = null;

        //Create a httpurlconnection to connect to internet
        HttpURLConnection httpURLConnection =null;
        //data from internet comes as an inputstream
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            //settings timeouts for the connection
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);

            //Get method is specified to retrieve data from server
            httpURLConnection.setRequestMethod("GET");

            //making actual connection to the server
            httpURLConnection.connect();

            //Check what is the response code for the get method
            if (httpURLConnection.getResponseCode() == 200) {
                debugLog(TAG,"Server connection is successful");
                inputStream = httpURLConnection.getInputStream();

                //extract the string data from inputstream
                responseString = extractStringFromStream(inputStream);
            } else {
                debugLog(TAG,"Failed to get response from sever");
            }


        } catch (IOException e) {
            e.printStackTrace();
            debugLog(TAG,"Error in getting server data");
        }finally {

            //cleanup connections incase of error/success
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //return the server returned data which is in json format
        return responseString;
    }

    private static String extractStringFromStream(InputStream inputStream) {
        //Here we extract string from inputstream which is in bits
        debugLog(TAG, "extractStringFromStream");
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in parsing server data");
        }
        return builder.toString();
    }

    private static MovieTitle createMovieFromResponse(String jsonResponse) {
        debugLog(TAG, "createMovieFromResponse");

        //Here a we parse the json string to get desired key value pairs


        /*
        Here is sample output
        {"Title":"Spiderman","Year":"1990","Rated":"N/A","Released":"N/A","Runtime":"5 min",
        "Genre":"Short","Director":"Christian Davi","Writer":"N/A","Actors":"N/A","Plot":"N/A",
        "Language":"German","Country":"Switzerland","Awards":"N/A","Poster":"N/A",
        "Ratings":[{"Source":"Internet Movie Database","Value":"5.7/10"}],"Metascore":"N/A",
        "imdbRating":"5.7","imdbVotes":"90","imdbID":"tt0100669","Type":"movie","DVD":"N/A",
        "BoxOffice":"N/A","Production":"N/A","Website":"N/A","Response":"True"}
        */

        JSONObject searchResult = null;
        try {
            //Entire response string is first converted into a json object
            searchResult = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Following lines retrieve specific values from the output string, see sample ouuput above
        //for keys

        String name = "";
        try {
            name = searchResult.getString("Title");

        } catch (JSONException e) {
            e.printStackTrace();
            //Return null if not movie with that name is found
            return null;
        }
        int year = 0;
        try {
            year = searchResult.getInt("Year");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String rating = " ";
        try {
            rating = searchResult.getString("imdbRating");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String language = "";
        try {
            language = searchResult.getString("Language");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String genre = "";
        try {
            genre = searchResult.getString("Genre");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        debugLog(TAG, String.format("Movie details are %s, %s, %s, %s, %s", name, language, year, rating, genre));

        //Since loader returns MovieTitle object to show to the user , lets create a MovieTitle
        //object from the data we have received from the server
        MovieTitle movieTitle = new MovieTitle(name, language, year, rating, genre);
        return movieTitle;
    }

    public static void debugLog(String tag, String msg) {
        Log.d(APP_LOG_TAG,tag+" --> "+msg);
    }

    public static MovieTitle fetchMovieData(String stringUrl) {
        debugLog(TAG, "fetchMovieData");

        //Perform sequence of steps to retrieve data from the server
        URL url = createUrl(stringUrl);
        if (url == null) {
            return null;
        }
        String response = getMovieData(url);
        if (response == null) {
            return null;
        }
        MovieTitle movieTitle = createMovieFromResponse(response);
        return movieTitle;
    }

}
