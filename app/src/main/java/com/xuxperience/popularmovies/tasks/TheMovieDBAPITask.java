package com.xuxperience.popularmovies.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.xuxperience.popularmovies.activities.MainActivity;
import com.xuxperience.popularmovies.listeners.AsyncTaskCompleteListener;
import com.xuxperience.popularmovies.models.MovieItem;
import com.xuxperience.popularmovies.utilities.NetworkUtils;
import com.xuxperience.popularmovies.utilities.TheMovieDBJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Interacts with theMovieDB API, and fetches movie info
 *
 * Created by badeyemi on 5/3/17.
 */

public class TheMovieDBAPITask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<MovieItem>> listener;

    public TheMovieDBAPITask(Context ctx,
                             AsyncTaskCompleteListener<ArrayList<MovieItem>> listener) {
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(URL... urls) {
        URL apiUrl = urls[0];
        String apiResult = null;
        ArrayList<MovieItem> moviesList = null;

        try {
            // Check if there's a network connection before making
            // API request
            if(NetworkUtils.isOnline(this.context)) {
                apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
//                    Log.d(LOG_TAG, "Result: \n" + apiResult);

                moviesList = TheMovieDBJSONUtils.getMovieItemsFromJSON(apiResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviesList;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItems) {
        super.onPostExecute(movieItems);

        if(listener != null) {
            listener.onTaskComplete(movieItems);
        }
    }
}