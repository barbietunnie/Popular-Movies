package com.xuxperience.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The movie object
 *
 * Created by badeyemi on 4/30/17.
 */

public class MovieItem implements Parcelable {
    Integer id;
    String title;
    String posterThumbnailUrl;
    String synopsis;
    Double userRating;
    String releaseDate;

    public MovieItem(Integer id, String title, String thumbnailUrl, String synopsis,
                     Double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterThumbnailUrl = thumbnailUrl;
        this.synopsis = synopsis;
        this.userRating = rating;
        this.releaseDate = releaseDate;
    }

    public MovieItem(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        posterThumbnailUrl = parcel.readString();
        synopsis = parcel.readString();
        userRating = parcel.readDouble();
        releaseDate = parcel.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(posterThumbnailUrl);
        parcel.writeString(synopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterThumbnailUrl() {
        return posterThumbnailUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return title;
    }
}
