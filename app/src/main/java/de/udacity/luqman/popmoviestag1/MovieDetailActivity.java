package de.udacity.luqman.popmoviestag1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.mtitle) TextView title;
    @BindView(R.id.mposter) ImageView poster;
    @BindView(R.id.mrelease) TextView releaseDate;
    @BindView(R.id.mdesc) TextView description;
    @BindView(R.id.mrating) TextView rating;
    @BindView(R.id.trailers_listview) ListView trailersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            Movie movie = bundle.getParcelable("movie");

            title.setText(movie.getTitle());
            releaseDate.setText(movie.getRelease_date());
            description.setText(movie.getOverview());
            rating.setText(movie.getVote_average());
            Picasso.with(this).load(movie.getPoster_path()).into(poster);


            //set Trailers
            subscribeService(MovieDBUtils.initilizeMovieDBService(),movie.getId(),new MovieDetailActivity.TrailerObserver(this));

        }



    }

    public class TrailerObserver implements Observer<TrailerResponse> {

        Context context;

        public TrailerObserver(Context context) {
            this.context = context;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e("xxxxx",e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onNext(TrailerResponse trailerResponse) {
            if(null!=trailerResponse && trailerResponse.getResults().size() > 0){
                final TrailerAdapter trailerAdapter = new TrailerAdapter((Activity) context,trailerResponse.getResults());
                trailersListView.setAdapter(trailerAdapter);
                MovieDBUtils.resizeListView(trailersListView);
                trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Trailer trailer = trailerAdapter.getItem(i);
                        String url = Constants.TRAILER_URL+trailer.getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
        }

    }

    public static void subscribeService(MovieDBService movieDBService,Long id,MovieDetailActivity.TrailerObserver trailerObserver){

        Observable<TrailerResponse> trailerObservable = movieDBService.getTrailers(id);

        trailerObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerObserver);

    }


}
