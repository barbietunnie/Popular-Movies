package com.xuxperience.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuxperience.popularmovies.AppConstants;
import com.xuxperience.popularmovies.R;
import com.xuxperience.popularmovies.models.MovieItem;

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

            if(movieBundle != null) {
                MovieItem movieItem = movieBundle.getParcelable(AppConstants.MOVIE_INTENT_KEY);
                if(null != movieItem) {
                    mMovieTitleTextView.setText(movieItem.getTitle());
                    mReleaseDateTextView.setText(movieItem.getReleaseDate());
                    mUserRatingTextView.setText(movieItem.getUserRating().toString() + "/10");
                    mSynopsisTextView.setText(movieItem.getSynopsis());

                    Picasso.with(this).load(movieItem.getPosterThumbnailUrl()).into(mPosterThumbnailImageView);
                }
            }
        }
    }
}
