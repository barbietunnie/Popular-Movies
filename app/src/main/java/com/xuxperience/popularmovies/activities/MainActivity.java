package com.xuxperience.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuxperience.popularmovies.AppConstants;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.adapters.MoviesAdapter;
import com.xuxperience.popularmovies.models.MovieItem;
import com.xuxperience.popularmovies.utilities.NetworkUtils;
import com.xuxperience.popularmovies.utilities.TheMovieDBJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String BUNDLE_ADAPTER_ITEMS = "movies";
    public static final String BUNDLE_SCROLL_POSITION = "scrollPos";

    // The movie sort order before state change
    public static final String BUNDLE_SORT_ORDER = "sortOrder";

    // The movies loader id
    public static final int MOVIES_LOADER_ID = 43;
    public static final String QUERY_MOVIES_SORT_ORDER_EXTRA = "query";

    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private MoviesAdapter mMoviesAdapter;
    private static String currentSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gv_movies_list);
        mGridView.setOnItemClickListener(this);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        currentSortOrder = getCurrentSortOrder();

        // Initialize the loader
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> themoviedbLoader = loaderManager.getLoader(MOVIES_LOADER_ID);

        if(themoviedbLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        currentSortOrder = getCurrentSortOrder();

        Log.d(LOG_TAG, "Current sort order: " + currentSortOrder);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(outState != null && mMoviesAdapter != null) {
            int scrollPosition = mGridView.getFirstVisiblePosition();

            outState.putInt(BUNDLE_SCROLL_POSITION, scrollPosition);

            Log.d(LOG_TAG, "Saving scroll postion: " + scrollPosition);

            // Save the current sort order
            outState.putString(BUNDLE_SORT_ORDER,
                    getSharedPreferences().getString(
                            getString(R.string.pref_sort_key),
                            getString(R.string.pref_sort_default_value)
                    ));
        }
    }

    /**
     * Restores the list of movies from the saved bundle
     *
     * @param savedInstanceState The saved bundle state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            // Restore the scroll position
            int scrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);

//            Log.d(LOG_TAG, "Restore state: Scroll Position: " + scrollPosition);

            if(scrollPosition > 0) {
                mGridView.smoothScrollToPosition(scrollPosition);
            }
        }
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
     * Show the loading indicator and hide the other components
     */
    private void showLoadingIndicator() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieItem selectedMovieItem = mMoviesAdapter.getItem(position);

        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(AppConstants.MOVIE_INTENT_KEY, selectedMovieItem);
        startActivity(detailActivityIntent);
    }

    /**
     * Gets the current setting of the sort order
     *
     * @return The current sort order
     */
    public String getCurrentSortOrder() {
        return getSharedPreferences().getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        );
    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieItem>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                // Show the loading indicator
                showLoadingIndicator();

                // Force a load
                forceLoad();
            }

            @Override
            public ArrayList<MovieItem> loadInBackground() {
                try {
                    // Check if there's a network connection before making
                    // API request
                    if(NetworkUtils.isOnline(getContext())) {
                        URL moviesApiURL = NetworkUtils.buildURL(getSharedPreferences().getString(
                                getString(R.string.pref_sort_key),
                                getString(R.string.pref_sort_default_value)
                        ));

                        String apiResult = NetworkUtils.getResponseFromHttpUrl(moviesApiURL);
                        Log.d(LOG_TAG, "Result: \n" + apiResult);

                        return TheMovieDBJSONUtils.getMovieItemsFromJSON(apiResult);
                    }

                    return null;
                } catch (IOException e) {
                    e.printStackTrace();

                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();

                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> moviesList) {
        if(moviesList != null && moviesList.size() > 0)  {
            mMoviesAdapter = new MoviesAdapter(this, moviesList);
            mGridView.setAdapter(mMoviesAdapter);

            showMoviesList();
        } else { // An error must have occurred
            showErrorMessage("Oops! It seems you're not connected to the internet. " +
                    "\nKindly check your internet settings");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {

    }
}
