package de.udacity.luqman.popmoviestag1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

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
            }else{
                this.setTitle(Constants.TOPMOVIE);
            }

        }

        this.setTitle(Constants.POPMOVIE);
        if(NetworkUtils.checkNetworkAval(this)) {
            subscribeService(MovieDBUtils.initilizeMovieDBService(),filter,new MovieDBObserver(this));
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
                subscribeService(MovieDBUtils.initilizeMovieDBService(),"popular",new MovieDBObserver(this));
                filter = "popular";
            }
            return true;

        }else if (id == R.id.action_top) {
            if(NetworkUtils.checkNetworkAval(this)) {
                this.setTitle(Constants.TOPMOVIE);
                subscribeService(MovieDBUtils.initilizeMovieDBService(),"top_rated",new MovieDBObserver(this));
                filter = "top_rated";
            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieDBObserver implements Observer<MovieResponse> {

        Context context;

        public MovieDBObserver(Context context) {
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


    public static void subscribeService(MovieDBService movieDBService,String filter,MovieDBObserver movieDBObserver){

        Observable<MovieResponse> moviesObservable = movieDBService.getMovies(filter,Constants.APIKEY);

        moviesObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieDBObserver);

    }



    private int calculateNoColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noColumns = (int) (dpWidth / 180);
        return noColumns;
    }
}
