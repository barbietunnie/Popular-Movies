package com.xuxperience.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.xuxperience.popularmovies.AppConstants;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.models.MovieItem;
import com.xuxperience.popularmovies.utilities.PopularMoviesDateUtils;

import java.text.ParseException;

public class DetailActivity extends BaseActivity {
    public static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView mMovieTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;
    private ImageView mPosterThumbnailImageView;
    private TextView mSynopsisTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterThumbnailImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);

        Intent parentIntent = getIntent();
        if(parentIntent != null) {
            Bundle movieBundle = parentIntent.getExtras();

            if(movieBundle != null && movieBundle.containsKey(AppConstants.MOVIE_INTENT_KEY)) {
                MovieItem movieItem = movieBundle.getParcelable(AppConstants.MOVIE_INTENT_KEY);
                if(null != movieItem) {
                    mMovieTitleTextView.setText(movieItem.getTitle());

                    String releaseDate = movieItem.getReleaseDate();

                    try {
                        Integer dateYr = PopularMoviesDateUtils.getYear(releaseDate);
                        mReleaseDateTextView.setText(dateYr.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        mReleaseDateTextView.setText("-");
                    }


                    mUserRatingTextView.setText(movieItem.getUserRating().toString() + "/10");
                    mSynopsisTextView.setText(movieItem.getSynopsis());

                    Picasso.with(this).load(movieItem.getPosterThumbnailUrl()).into(mPosterThumbnailImageView);
                }
            }
        }
    }

    /**
     * Invoked when the favorite button is clicked
     *
     * @param view
     */
    public void onFavoriteClicked(View view) {
        ToggleButton toggleButton = (ToggleButton) view;

        boolean markAsFavorite = toggleButton.isChecked();

        // TODO: Save in database or remove from database
        // based on the choice selected
    }
}
