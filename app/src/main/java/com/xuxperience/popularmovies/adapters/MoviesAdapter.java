package com.xuxperience.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.models.MovieItem;

import java.util.ArrayList;

/**
 * The Movies adapter
 *
 * Created by badeyemi on 4/30/17.
 */

public class MoviesAdapter extends BaseAdapter {
    public static final String LOG_TAG = MoviesAdapter.class.getSimpleName();


    private ArrayList<MovieItem> moviesList;
    private Context context;

    public MoviesAdapter(Context context,
                         ArrayList<MovieItem> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @Override
    public int getCount() {
        int count = 0;

        if(moviesList != null)
            count = moviesList.size();

        return count;
    }

    @Override
    public MovieItem getItem(int i) {
        MovieItem movieItem = null;
        if(moviesList != null)
            movieItem = moviesList.get(i);

        return movieItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d(LOG_TAG, "getView: " + position);
        // Get the MovieItem object from the array list at the appropriate position
        MovieItem movieItem = moviesList.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(
                    R.layout.movie_item, parent, false);
        }

        if(movieItem != null) {
            String posterThumbnailUrl = movieItem.getPosterThumbnailUrl();

            ImageView posterView = (ImageView) convertView.findViewById(R.id.iv_movie_item);
            Picasso.with(this.context).load(posterThumbnailUrl).into(posterView);

            TextView movieTitleView = (TextView) convertView.findViewById(R.id.tv_movie_title);
            movieTitleView.setText(movieItem.getTitle());
        }

        return convertView;
    }
}
