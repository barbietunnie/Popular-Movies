package com.xuxperience.popularmovies.utilities;

import android.content.Context;

import com.xuxperience.popularmovies.models.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility functions to handle TheMovieDB JSON data
 *
 * Created by badeyemi on 4/30/17.
 */

public class TheMovieDBJSONUtils {
    public static final String TMDB_POSTER_THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String TMDB_PREFERRED_FILE_SIZE = "w185";

    public static ArrayList<MovieItem> getMovieItemsFromJSON(String moviesJsonStr) throws JSONException {
        ArrayList<MovieItem> movies = null;

        if(moviesJsonStr != null) {
            /** Movie information. Each movie information is an element of the "results" array */
            final String TMDB_RESULTS = "results";

            final String TMDB_MOVIE_TITLE = "original_title";
            final String TMDB_MOVIE_RATING = "vote_average";
            final String TMDB_MOVIE_OVERVIEW = "overview";
            final String TMDB_MOVIE_POSTER = "poster_path";
            final String TMDB_MOVIE_ID = "id";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            if(null != moviesJson) {
                movies = new ArrayList<>();

                JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);
                for (int i = 0; i < moviesArray.length(); i++) {
                    Integer id;
                    String title;
                    String releaseDate;
                    String synopsis;
                    Double rating;
                    String thumbnailUrl;

                    JSONObject movieObject = moviesArray.getJSONObject(i);
                    id = movieObject.getInt(TMDB_MOVIE_ID);
                    title = movieObject.getString(TMDB_MOVIE_TITLE);
                    releaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                    synopsis = movieObject.getString(TMDB_MOVIE_OVERVIEW);
                    rating = movieObject.getDouble(TMDB_MOVIE_RATING);

                    thumbnailUrl = TMDB_POSTER_THUMBNAIL_BASE_URL +
                            TMDB_PREFERRED_FILE_SIZE + movieObject.getString(TMDB_MOVIE_POSTER);

                    MovieItem movie = new MovieItem(id, title, thumbnailUrl, synopsis, rating, releaseDate);
                    movies.add(movie);
                }
            }
        }

        return movies;
    }
}
