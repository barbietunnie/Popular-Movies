package com.xuxperience.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.xuxperience.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Network utility methods
 *
 * Created by badeyemi on 4/30/17.
 */

public final class NetworkUtils {
    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static final String THEMOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String PARAM_API_KEY = "api_key";

    /** The query strings for the different movie endpoints */
    public static final String MOST_POPULAR_ENDPOINT = "popular";
    public static final String HIGHEST_RATED_ENDPOINT = "top_rated";

    /** The path for the trailers and reviews endpoints **/
    public static final String REVIEWS_PATH = "reviews";
    public static final String TRAILERS_PATH = "videos";

    /**
     * Builds the URL used to fetch movies
     *
     * @param type The sort order type
     * @return The built URL
     */
    public static URL buildURL(String type) {
        String endpoint = null;

        endpoint = type;
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                        .appendPath(endpoint)
                        .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

        Log.d(LOG_TAG, "Built URI: " + builtUri);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL for a particular movies meta-data
     *
     * @param movieId The movie id
     * @param type The meta-data type (reviews, trailers etc)
     * @return The built Uri
     */
    public static URL buildMovieMetaDataURL(String movieId, String type) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(type)
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        Log.d(LOG_TAG, "Meta-data URI: " + builtUri);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Returns the entire result from a URL
     *
     * @param url The URL to fetch data from
     * @return The contents of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Check for internet access on the device
     *
     * @param ctx The application context
     * @return
     */
    public static boolean isOnline(Context ctx) {
        ConnectivityManager cMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cMgr != null) {
            NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        return false;
    }
}
