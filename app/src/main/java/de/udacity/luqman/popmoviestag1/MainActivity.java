package de.udacity.luqman.popmoviestag1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviePosters;

    private String filter = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviePosters = (RecyclerView) findViewById(R.id.movie_posters);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
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
        new MovieDBTask(this).execute(filter);

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

            this.setTitle(Constants.POPMOVIE);
            new MovieDBTask(this).execute("popular");
            filter = "popular";
            return true;

        }else if (id == R.id.action_top) {
            this.setTitle(Constants.TOPMOVIE);
            new MovieDBTask(this).execute("top_rated");
            filter = "top_rated";
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieDBTask extends AsyncTask<String,Void,ArrayList<Movie>>{
        Context context;

        public MovieDBTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... filter) {

            try {
                return MovieDBUtils.downloadMovies(filter[0]);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected  void onPostExecute(ArrayList<Movie> movieList){

            if(null!=movieList && movieList.size() > 0){
                moviesAdapter = new MoviesAdapter(context, movieList);
                moviePosters.setAdapter(moviesAdapter);
            }

        }
    }
}
