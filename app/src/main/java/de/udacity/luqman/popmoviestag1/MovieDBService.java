package de.udacity.luqman.popmoviestag1;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by luqman on 13.04.2017.
 */

public interface MovieDBService {

    @GET("movie/{filter}?api_key="+Constants.APIKEY)
    public Observable<MovieResponse> getMovies(@Path("filter") String filter);

    @GET("movie/{id}/videos?api_key="+Constants.APIKEY)
    public Observable<TrailerResponse> getTrailers(@Path("id") Long id);

    @GET("movie/{id}/reviews?api_key="+Constants.APIKEY)
    public Observable<ReviewResponse> getReviews(@Path("id") Long id);

}
