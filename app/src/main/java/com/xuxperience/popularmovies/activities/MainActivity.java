package com.xuxperience.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuxperience.popularmovies.AppConstants;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.adapters.MoviesAdapter;
import com.xuxperience.popularmovies.listeners.AsyncTaskCompleteListener;
import com.xuxperience.popularmovies.models.MovieItem;
import com.xuxperience.popularmovies.tasks.TheMovieDBAPITask;
import com.xuxperience.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        AsyncTaskCompleteListener<ArrayList<MovieItem>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String BUNDLE_ADAPTER_ITEMS = "movies";
    public static final String BUNDLE_SCROLL_POSITION = "scrollPos";

    // The movie sort order before state change
    public static final String BUNDLE_SORT_ORDER = "sortOrder";

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

        restoreMoviesList(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(outState != null && mMoviesAdapter != null) {
            outState.putParcelableArrayList(BUNDLE_ADAPTER_ITEMS, mMoviesAdapter.getMoviesList());

            int scrollPosition = mGridView.getFirstVisiblePosition();

            outState.putInt(BUNDLE_SCROLL_POSITION, scrollPosition);

            // Save the current sort order
            outState.putString(BUNDLE_SORT_ORDER,
                    getSharedPreferences().getString(
                            getString(R.string.pref_sort_key),
                            getString(R.string.pref_sort_default_value)
                    ));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        restoreMoviesList(savedInstanceState);
    }

    /**
     * Restores the list of movies from the saved bundle
     *
     * @param savedInstanceState The saved bundle state
     */
    private void restoreMoviesList(Bundle savedInstanceState) {
        boolean loaded = false;

        if(savedInstanceState != null) {
            ArrayList<MovieItem> moviesList = savedInstanceState.getParcelableArrayList(BUNDLE_ADAPTER_ITEMS);
            if(moviesList != null && moviesList.size() > 0) {
                onLoadFinished(moviesList);

                loaded = true;

                // Restore the scroll position
                int scrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
                if(scrollPosition > 0) {
                    mGridView.smoothScrollToPosition(scrollPosition);
                }
            }
        }

        if(!loaded) {
//            Log.d(LOG_TAG, "No saved movies found");
            loadMoviesData();
        }
    }

    private void loadMoviesData() {
        URL moviesURL = NetworkUtils.buildURL(getSharedPreferences().getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        ));
        new TheMovieDBAPITask(this, this).execute(moviesURL);
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

        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(AppConstants.MOVIE_INTENT_KEY, selectedMovieItem);
        startActivity(detailActivityIntent);
    }

    @Override
    public void onTaskComplete(ArrayList<MovieItem> items) {
        onLoadFinished(items);
    }
}
