package de.udacity.luqman.popmoviestag1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luqman on 02.02.2017.
 */

public class Movie implements Parcelable {


    private String title;

    private String overview;

    private String vote_average;

    private String release_date;

    private String poster_path;

    public Movie() {}

    public Movie(String title, String overview, String userRatings, String release_date, String poster_path) {
        this.title = title;
        this.overview = overview;
        this.vote_average = userRatings;
        this.release_date = release_date;
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date.split("-")[0];
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average.concat("/10");
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path()
    {
        return "http://image.tmdb.org/t/p/w185/"+ poster_path;
    }

    public void setPoster_path(String poster_path) {

        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
    }


}
