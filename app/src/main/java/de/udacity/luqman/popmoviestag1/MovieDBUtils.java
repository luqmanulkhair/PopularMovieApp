package de.udacity.luqman.popmoviestag1;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luqman on 02.02.2017.
 */

public class MovieDBUtils {


    public static MovieDBService initilizeMovieDBService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.MOVIEDB_ENDPOINT)
                .build();

        MovieDBService movieDBService = retrofit.create(MovieDBService.class);

        return movieDBService;
    }









}
