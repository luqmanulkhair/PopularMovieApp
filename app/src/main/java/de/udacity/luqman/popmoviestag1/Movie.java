package de.udacity.luqman.popmoviestag1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luqman on 02.02.2017.
 */

public class Movie implements Parcelable {


    private String originalTitle;

    private String plotSynopsis;

    private String userRatings;

    private String releaseDate;

    private String posterPath;

    public Movie(String originalTitle, String plotSynopsis, String userRatings, String releaseDate, String posterPath) {
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.userRatings = userRatings;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(String userRatings) {
        this.userRatings = userRatings;
    }

    public String getPosterPath()
    {
        return "http://image.tmdb.org/t/p/w185/"+ posterPath;
    }

    public void setPosterPath(String posterPath) {

        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(plotSynopsis);
        parcel.writeString(userRatings);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
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
        originalTitle = in.readString();
        plotSynopsis = in.readString();
        userRatings = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
    }
}
