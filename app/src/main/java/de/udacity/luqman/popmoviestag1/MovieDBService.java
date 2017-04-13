package de.udacity.luqman.popmoviestag1;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luqman on 13.04.2017.
 */

public interface MovieDBService {



    @GET("movie/{filter}")
    public Observable<MovieResponse> getMovies(@Path("filter") String filter, @Query("api_key") String apiKey);

}
