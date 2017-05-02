package com.xuxperience.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuxperience.popularmovies.AppConstants;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.adapters.MoviesAdapter;
import com.xuxperience.popularmovies.models.MovieItem;
import com.xuxperience.popularmovies.utilities.TheMovieDBJSONUtils;
import com.xuxperience.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gv_movies_list);
        mGridView.setOnItemClickListener(this);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        loadMoviesData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    private void loadMoviesData() {
        Log.d(LOG_TAG, "Default preference: " + getSharedPreferences().getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        ));

//        URL moviesURL = NetworkUtils.buildURL(NetworkUtils.SortOrderType.MOST_POPULAR);
        URL moviesURL = NetworkUtils.buildURL(getSharedPreferences().getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        ));
        new TheMovieDBAPITask().execute(moviesURL);
    }

    /**
     * Show only the error message and hide every other component
     * on screen
     *
     * @param msg The error message to display
     */
    private void showErrorMessage(String msg) {
        mGridView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setText(msg);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Show the list of movies and hide every other component on screen
     */
    private void showMoviesList() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the movies have been loaded from theMovieDB
     */
    public void onLoadFinished(ArrayList<MovieItem> movies) {
        if(movies != null && movies.size() > 0)  {
            mMoviesAdapter = new MoviesAdapter(this, movies);
            mGridView.setAdapter(mMoviesAdapter);

            showMoviesList();
        } else { // An error must have occurred
            showErrorMessage("Oops! It seems you're not connected to the internet. \nKindly check your internet settings");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieItem selectedMovieItem = mMoviesAdapter.getItem(position);

//        Log.d(LOG_TAG, "Selected movie: " + selectedMovieItem);

        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(AppConstants.MOVIE_INTENT_KEY, selectedMovieItem);
        startActivity(detailActivityIntent);
    }

    class TheMovieDBAPITask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {

        @Override
        protected ArrayList<MovieItem> doInBackground(URL... urls) {
            URL apiUrl = urls[0];
            String apiResult = null;
            ArrayList<MovieItem> moviesList = null;

            try {
                // Check if there's a network connection before making
                // API request
                if(NetworkUtils.isOnline(MainActivity.this)) {
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
            onLoadFinished(movieItems);
        }
    }
}
