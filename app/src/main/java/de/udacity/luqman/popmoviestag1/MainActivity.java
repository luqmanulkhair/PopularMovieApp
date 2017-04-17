package de.udacity.luqman.popmoviestag1;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity{

    private MoviesAdapter moviesAdapter;
    @BindView(R.id.movie_posters) RecyclerView moviePosters;

    private String filter = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,calculateNoColumns(this));
        moviePosters.setLayoutManager(gridLayoutManager);
        moviePosters.setHasFixedSize(true);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            filter = savedInstanceState.getString("filter");
            if(filter.equalsIgnoreCase("popular")){
                this.setTitle(Constants.POPMOVIE);
                if (NetworkUtils.checkNetworkAval(this)) {
                    subscribeService(MovieDBUtils.initilizeMovieDBService(), filter, new MovieObserver(this));
                }
            }else if(filter.equalsIgnoreCase("top_rated")){
                this.setTitle(Constants.TOPMOVIE);
                if (NetworkUtils.checkNetworkAval(this)) {
                    subscribeService(MovieDBUtils.initilizeMovieDBService(), filter, new MovieObserver(this));
                }
            }else{
                this.setTitle(Constants.FAVMOVIE);
                loadContentProvider();
            }

        }else {
            this.setTitle(Constants.POPMOVIE);
            if (NetworkUtils.checkNetworkAval(this)) {
                subscribeService(MovieDBUtils.initilizeMovieDBService(), filter, new MovieObserver(this));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filter",filter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        filter = savedInstanceState.getString("filter");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pop) {
            if(NetworkUtils.checkNetworkAval(this)){
                this.setTitle(Constants.POPMOVIE);
                subscribeService(MovieDBUtils.initilizeMovieDBService(),"popular",new MovieObserver(this));
                filter = "popular";
            }
            return true;

        }else if (id == R.id.action_top) {
            if(NetworkUtils.checkNetworkAval(this)) {
                this.setTitle(Constants.TOPMOVIE);
                subscribeService(MovieDBUtils.initilizeMovieDBService(),"top_rated",new MovieObserver(this));
                filter = "top_rated";
            }
            return true;

        }else if (id == R.id.action_fav) {
            if(NetworkUtils.checkNetworkAval(this)) {
                this.setTitle(Constants.FAVMOVIE);
                loadContentProvider();
            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieObserver implements Observer<MovieResponse> {

        Context context;

        public MovieObserver(Context context) {
            this.context = context;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(MovieResponse movieResponse) {
            if(null!=movieResponse && movieResponse.getResults().size() > 0){
                moviesAdapter = new MoviesAdapter(context, movieResponse.getResults());
                moviePosters.setAdapter(moviesAdapter);
            }
        }

    }


    public static void subscribeService(MovieDBService movieDBService,String filter,MovieObserver movieObserver){

        Observable<MovieResponse> moviesObservable = movieDBService.getMovies(filter);

        moviesObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieObserver);

    }

    private void loadContentProvider(){
        Cursor moviesCursor = this.getContentResolver().query(
                FavMovieContract.FavMovieEntry.CONTENT_URI,
                new String[]{
                        FavMovieContract.FavMovieEntry._ID,
                        FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID,
                        FavMovieContract.FavMovieEntry.COLUMN_TITLE,
                        FavMovieContract.FavMovieEntry.COLUMN_POSTER,
                        FavMovieContract.FavMovieEntry.COLUMN_YEAR,
                        FavMovieContract.FavMovieEntry.COLUMN_RATING,
                        FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION,


                },null,null,null
        );



        if (null != moviesCursor && moviesCursor.moveToFirst()) {
            ArrayList<Movie> moviesList = new ArrayList<Movie>();
            while (moviesCursor.isAfterLast() == false) {

                Movie movie = new Movie();
                movie.setId(Long.valueOf(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_ID))).longValue());
                movie.setOverview(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION)));
                movie.setPoster_path(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_POSTER)));
                movie.setRelease_date(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_YEAR)));
                movie.setTitle(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_TITLE)));
                movie.setVote_average(moviesCursor.getString(moviesCursor.getColumnIndex(FavMovieContract.FavMovieEntry.COLUMN_RATING)));

                moviesList.add(movie);
                moviesCursor.moveToNext();
            }




            moviesAdapter = new MoviesAdapter(this, moviesList);
            moviePosters.setAdapter(moviesAdapter);
            filter = "fav";

        }

    }



    private int calculateNoColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noColumns = (int) (dpWidth / 180);
        return noColumns;
    }
}
