package com.xuxperience.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xuxperience.popularmovies.R;

import java.util.zip.Inflater;

/**
 * This custom CursorAdapter creates and binds ViewHolders, that hold the movie information,
 * to the Recyclerview to efficiently display data
 *
 * Created by badeyemi on 6/10/17.
 */

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MovieItemHolder> {
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor for the MoviesCursorAdapter that initializes the context
     *
     * @param mContext The current context
     */
    public MoviesCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView
     *
     * @param parent The parent view group
     * @param viewType The view type
     * @return The new MovieItemHolder that holds the view for each task
     */
    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate movie item layout to a view
        View view = LayoutInflater.from(mContext)
                                    .inflate(R.layout.movie_item, parent, false);

        return new MovieItemHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor
     *
     * @param holder The movie item ViewHolder
     * @param position The item position
     */
    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {

    }

    /**
     * Returns the number of items to display
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if(mCursor == null) {
            return 0;
        }

        return mCursor.getCount();
    }

    // Inner class for creating ViewHolders
    class MovieItemHolder extends RecyclerView.ViewHolder {
        ImageView posterView;

        /**
         * Constructor for the MovieItemHolders
         *
         * @param itemView The view inflated in OnCreateViewHolder
         */
        public MovieItemHolder(View itemView) {
            super(itemView);

            posterView = (ImageView) itemView.findViewById(R.id.iv_movie_item);
        }
    }
}
